package com.sunragav.indiecampers.home.domain.usecases

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.home.domain.qualifiers.Foreground
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import com.sunragav.indiecampers.home.domain.usecases.base.ObservableUseCase
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject


class GetComicsAction @Inject constructor(
    private val comicsDataRepository: ComicsDataRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : ObservableUseCase<ComicsEntity, String>(backgroundScheduler, foregroundScheduler) {

    override fun generateObservable(input: String): Observable<ComicsEntity> {
        return comicsDataRepository.getComics(input)
    }
}