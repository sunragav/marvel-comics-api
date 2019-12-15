package com.sunragav.indiecampers.home.domain.usecases.base

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class ObservableUseCase<T, in Input> constructor(
    protected val backgroundScheduler: Scheduler,
    protected val foregroundScheduler: Scheduler
) {
    protected abstract fun generateObservable(input: Input? = null): Observable<T>

    fun buildUseCase(input: Input? = null): Observable<T> {
        return generateObservable(input)
            .subscribeOn(backgroundScheduler)
            .observeOn(foregroundScheduler)
    }

}