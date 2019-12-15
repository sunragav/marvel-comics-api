package com.sunragav.indiecampers.home.presentation.viewmodels


import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.UpdateComicsAction
import com.sunragav.indiecampers.home.presentation.mapper.Mapper
import com.sunragav.indiecampers.home.presentation.models.Comics
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class HomeVM @Inject internal constructor(
    private val comicsMapper: Mapper<ComicsEntity, Comics>,
    private val getComicsListAction: GetComicsListAction,
    private val updateComicsAction: UpdateComicsAction
) : ViewModel() {
    companion object {
        const val LIMIT = 40
    }

    private lateinit var dataSource: DataSource.Factory<Int, ComicsEntity>
    private lateinit var boundaryCallback: PagedList.BoundaryCallback<ComicsEntity>
    private val filterRequestLiveData = MutableLiveData<GetComicsListAction.Params>()
    private var filterRequest = GetComicsListAction.Params()


    private val disposables = CompositeDisposable()
    private val comicsListMediator = MediatorLiveData<PagedList<ComicsEntity>>()

    val comicsListSource: LiveData<PagedList<ComicsEntity>>
        get() = comicsListMediator
    var networkState: BehaviorRelay<NetworkState> = BehaviorRelay.create()


    private val filteredComicsBySearchKey =
        Transformations.switchMap(filterRequestLiveData) { input ->
            getComicsListAction.buildUseCase(input)
                .map {
                    disposables.add(it.networkState.subscribe(networkState))
                    dataSource = it.dataSource
                    boundaryCallback = it.boundaryCallback
                    dataSource
                }
                .toFlowable(BackpressureStrategy.LATEST)
                .toLiveData()
        }


    init {
        networkState.accept(NetworkState.EMPTY)
        comicsListMediator.addSource(filteredComicsBySearchKey) {
            comicsListMediator.value = LivePagedListBuilder(it, LIMIT).build().value
        }
    }

    fun search(key: String) {
        filterRequest = filterRequest.copy(
            searchKey = key,
            flagged = false
        )
        filterRequestLiveData.postValue(filterRequest)
    }

    fun fetchComicsList(key: String) {
        filterRequest = filterRequest.copy(
            searchKey = key,
            flagged = true
        )
        filterRequestLiveData.postValue(filterRequest)
    }

    fun toggleFlaggedStatus(comics: Comics) {
        val newComics = comicsMapper.from(
            comics.copy(
                flagged = !comics.flagged /*toggled status*/
            )
        )
        disposables.add(
            updateComicsAction
                .buildUseCase(newComics)
                .onErrorComplete()
                .subscribe()
        )
    }

    private fun resetFilterOptions() {
        filterRequest = filterRequest.copy(
            searchKey = "",
            flagged = false
        )
        filterRequestLiveData.postValue(filterRequest)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}