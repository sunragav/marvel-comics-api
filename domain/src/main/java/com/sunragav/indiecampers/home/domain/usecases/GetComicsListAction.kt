package com.sunragav.indiecampers.home.domain.usecases

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.home.domain.qualifiers.Foreground
import com.sunragav.indiecampers.home.domain.repositories.ComicsRepository
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import com.sunragav.indiecampers.home.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

class GetComicsListAction @Inject constructor(
    private val comicsRepository: ComicsRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : ObservableUseCase<GetComicsListActionResult, GetComicsListAction.Params>(
    backgroundScheduler,
    foregroundScheduler
) {
    companion object {
        const val PAGE_SIZE = 20
    }


    data class Params(
        val searchKey: String = "",
        val flagged: Boolean = false,
        val limit: Int = PAGE_SIZE,
        val networkState: BehaviorRelay<NetworkState>
    )

    override fun generateObservable(input: Params): Observable<GetComicsListActionResult> {
        return Observable.fromCallable { comicsRepository.getComicsList(input) }
    }

    class GetComicsListActionResult(
        val dataSource: DataSource.Factory<Int, ComicsEntity>,
        val boundaryCallback: PagedList.BoundaryCallback<ComicsEntity>
    )


}