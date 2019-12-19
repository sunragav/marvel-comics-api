package com.sunragav.indiecampers.home.presentation.viewmodels


import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.Params
import com.sunragav.indiecampers.home.domain.usecases.UpdateComicsAction
import com.sunragav.indiecampers.home.presentation.models.Comics
import com.sunragav.indiecampers.utils.Mapper
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class HomeVM @Inject internal constructor(
    private val disposables: CompositeDisposable,
    private val comicsMapper: Mapper<ComicsEntity, Comics>,
    private val getComicsListAction: GetComicsListAction,
    private val updateComicsAction: UpdateComicsAction,
    networkStateRelay: NetworkStateRelay
) : ViewModel(), CoroutineScope {
    companion object {
        private const val LIMIT = 50
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val bgScope = Dispatchers.IO
    val filterRequestLiveData = MutableLiveData<Params>()


    var currentComics = MutableLiveData<ComicsEntity>()

    private var filterRequest = Params(limit = LIMIT)
    val isLoading = ObservableField<Boolean>()

    fun lastSearchQuery() = filterRequestLiveData.value?.searchKey
    val comicsListSource: LiveData<LiveData<PagedList<ComicsEntity>>>
        get() = mutablComicsListSource

    private val mutablComicsListSource = MutableLiveData<LiveData<PagedList<ComicsEntity>>>()
    fun loadData() {
        filterRequestLiveData.value?.let { param ->
            uiScope.launch {
                withContext(bgScope) {
                    with(getComicsListAction.getComicsListActionResult(param)) {
                        mutablComicsListSource.postValue(
                            LivePagedListBuilder(dataSource, pagingConfig)
                                .setBoundaryCallback(boundaryCallback)
                                .build()
                        )
                    }
                }
            }
        }
    }


    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(LIMIT)
        .setPageSize(LIMIT)
        .build()


    init {
        networkStateRelay.relay.accept(NetworkState.EMPTY)
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