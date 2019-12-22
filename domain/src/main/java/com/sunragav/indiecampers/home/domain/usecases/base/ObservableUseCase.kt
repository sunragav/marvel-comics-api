package com.sunragav.indiecampers.home.domain.usecases.base

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class ObservableUseCase<T, in Input> constructor(
    private val backgroundScheduler: Scheduler,
    private val foregroundScheduler: Scheduler
) {
    protected abstract fun generateObservable(input: Input): Observable<T>

    fun buildUseCase(input: Input): Observable<T> {
        return generateObservable(input)
            .subscribeOn(backgroundScheduler)
            .observeOn(foregroundScheduler)
    }

}