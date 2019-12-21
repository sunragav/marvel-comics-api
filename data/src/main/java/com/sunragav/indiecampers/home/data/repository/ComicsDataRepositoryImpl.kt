package com.sunragav.indiecampers.home.data.repository

import androidx.paging.PagedList
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.home.domain.qualifiers.Foreground
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComicsDataRepositoryImpl @Inject constructor(
    val disposable: CompositeDisposable,
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository,
    val networkStateRelay: NetworkStateRelay,
    @Foreground val foregroundScheduler: Scheduler,
    @Background val backgroundScheduler: Scheduler
) : ComicsDataRepository {

    override fun getComics(query: String): Observable<ComicsEntity> {
        val localObservable = localRepository.getComicsById(query)
        val remoteObservable = remoteRepository.getComicsById(query).doOnNext {
            updateComics(it)
        }
        return Observable.concat(localObservable, remoteObservable).firstOrError().toObservable()
    }

    override fun getComicsList(query: GetComicsListAction.Params): GetComicsListActionResult {
        return GetComicsListActionResult(
            localRepository.getComicsListDatasourceFactory(query),
            ComicsListBoundaryCallback(query)
        )
    }


    override fun updateComics(comicsEntity: ComicsEntity): Completable {
        return localRepository.update(comicsEntity)
    }

    inner class ComicsListBoundaryCallback(
        private val query: GetComicsListAction.Params
    ) : PagedList.BoundaryCallback<ComicsEntity>() {

        private var lastRequestedPage = 0


        private var isRequestInProgress = false

        override fun onZeroItemsLoaded() = requestAndSaveData(query)

        override fun onItemAtEndLoaded(itemAtEnd: ComicsEntity) =
            requestAndSaveData(query)

        private fun requestAndSaveData(query: GetComicsListAction.Params) {

            if (isRequestInProgress) {
                return
            }
            networkStateRelay.relay.accept(NetworkState.LOADING)
            println("Requesting page$lastRequestedPage")
            isRequestInProgress = true
            disposable.add(
                remoteRepository.getComicsList(
                    query.searchKey,
                    lastRequestedPage,
                    query.limit
                ).subscribeOn(backgroundScheduler).observeOn(backgroundScheduler).subscribe(
                    { comicsList ->
                        println("Loaded page$lastRequestedPage offset:${lastRequestedPage * query.limit}")
                        localRepository.insert(comicsList)
                            .subscribeOn(backgroundScheduler)
                            .observeOn(backgroundScheduler)
                            .subscribe {
                                lastRequestedPage++
                                isRequestInProgress = false
                                localRepository.updateRequest(query.copy(offset = lastRequestedPage))
                                Observable.fromCallable {
                                    networkStateRelay.relay.accept(NetworkState.LOADED)
                                }.subscribeOn(foregroundScheduler)
                                    .retry(1)
                                    .subscribe()
                            }
                    },
                    { error ->
                        Observable.fromCallable {
                            networkStateRelay.relay.accept(NetworkState.error(error.localizedMessage))
                        }.subscribeOn(foregroundScheduler)
                            .subscribe()
                        isRequestInProgress = false
                    })
            )

        }
    }
}