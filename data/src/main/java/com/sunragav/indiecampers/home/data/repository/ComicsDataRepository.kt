package com.sunragav.indiecampers.data.repository

import androidx.paging.PagedList
import com.sunragav.indiecampers.home.data.repository.LocalRepository
import com.sunragav.indiecampers.home.data.repository.RemoteRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.repositories.ComicsRepository
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComicsDataRepository @Inject constructor(
    val localRepository: LocalRepository,
    val remoteRepository: RemoteRepository
) : ComicsRepository {

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
            RepoBoundaryCallback(query)
        )
    }


    override fun updateComics(comicsEntity: ComicsEntity): Completable {
        return localRepository.update(comicsEntity)
    }

    inner class RepoBoundaryCallback(
        private val query: GetComicsListAction.Params
    ) : PagedList.BoundaryCallback<ComicsEntity>() {

        private var lastRequestedPage = 1


        private var isRequestInProgress = false

        override fun onZeroItemsLoaded() = requestAndSaveData(query)

        override fun onItemAtEndLoaded(itemAtEnd: ComicsEntity) =
            requestAndSaveData(query)

        private fun requestAndSaveData(query: GetComicsListAction.Params) {
            if (isRequestInProgress) return
            query.networkState.accept(NetworkState.LOADING)
            isRequestInProgress = true
            remoteRepository.getComicsList(
                query.searchKey,
                lastRequestedPage,
                query.limit,
                { comicsList ->
                    localRepository.insert(comicsList) {
                        query.networkState.accept(NetworkState.LOADED)
                        lastRequestedPage++
                        isRequestInProgress = false
                    }
                },
                { error ->
                    query.networkState.accept(NetworkState.error(error.localizedMessage))
                    isRequestInProgress = false
                })
        }
    }
}