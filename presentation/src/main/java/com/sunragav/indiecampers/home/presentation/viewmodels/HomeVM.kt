package com.sunragav.indiecampers.home.presentation.viewmodels


import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.Params
import com.sunragav.indiecampers.home.domain.usecases.UpdateComicsAction
import com.sunragav.indiecampers.home.presentation.models.Comics
import com.sunragav.indiecampers.utils.Mapper
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class HomeVM @Inject internal constructor(
    private val comicsMapper: Mapper<ComicsEntity, Comics>,
    private val getComicsListAction: GetComicsListAction,
    private val updateComicsAction: UpdateComicsAction
) : ViewModel() {
    companion object {
        private const val LIMIT = 40
    }

    private val filterRequestLiveData = MutableLiveData<Params>()
    var networkState: BehaviorRelay<NetworkState> = BehaviorRelay.create()
    var currentComics = MutableLiveData<ComicsEntity>()

    private var filterRequest = Params(limit = LIMIT, networkState = networkState)
    private val disposables = CompositeDisposable()
    val isLoading = ObservableField<Boolean>()

    fun lastSearchQuery() = filterRequestLiveData.value?.searchKey

    private val result =
        Transformations.map(filterRequestLiveData) { input ->
            getComicsListAction.buildUseCase(input).blockingFirst()
        }

    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(LIMIT)
        .setPageSize(LIMIT)
        .build()


    val comicsListSource: LiveData<PagedList<ComicsEntity>> = Transformations.switchMap(result) {
        LivePagedListBuilder(it.dataSource, pagingConfig)
            .setBoundaryCallback(it.boundaryCallback)
            .build()
    }


    init {
        networkState.accept(NetworkState.EMPTY)
    }

    fun search(key: String) {
        filterRequest = filterRequest.copy(
            searchKey = key,
            flagged = false
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