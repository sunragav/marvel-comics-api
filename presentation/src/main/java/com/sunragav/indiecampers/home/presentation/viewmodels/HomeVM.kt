package com.sunragav.indiecampers.home.presentation.viewmodels


import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.Params
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class HomeVM @Inject internal constructor(
    private val disposables: CompositeDisposable,
    private val getComicsListAction: GetComicsListAction,
    networkStateRelay: NetworkStateRelay
) : ViewModel(), CoroutineScope {
    companion object {
        private const val LIMIT = 20
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val bgScope = Dispatchers.IO

    private val filterRequestLiveData = MutableLiveData<Params>()

    private val defaultComics = ComicsEntity(
        id = "default",
        title = "",
        description = "",
        thumbNail = "",
        imageUrls = emptyList(),
        flagged = false
    )

    var currentComics = MutableLiveData<ComicsEntity>()

    private var filterRequest = Params(limit = LIMIT)
    val isLoading = ObservableField<Boolean>()

    fun lastSearchQuery() = filterRequestLiveData.value?.searchKey

    private val pagingConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(LIMIT)
        .setPageSize(LIMIT)
        .build()


    private val searchResult = Transformations.map(filterRequestLiveData) { param ->
        with(getComicsListAction.getComicsListActionResult(param)) {
            LivePagedListBuilder(dataSource, pagingConfig)
                .setBoundaryCallback(boundaryCallback)
                .build()
        }
    }

    val comicsListSource = Transformations.switchMap(searchResult) { it }

    init {
        networkStateRelay.relay.accept(NetworkState.EMPTY)
        currentComics.value = defaultComics

    }

    fun search(key: String) {
        filterRequest = filterRequest.copy(
            searchKey = key,
            flagged = false
        )
        filterRequestLiveData.postValue(filterRequest)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}