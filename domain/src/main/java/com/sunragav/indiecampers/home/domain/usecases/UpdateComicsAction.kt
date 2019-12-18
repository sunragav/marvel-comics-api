package com.sunragav.indiecampers.home.domain.usecases

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.home.domain.qualifiers.Foreground
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import com.sunragav.indiecampers.home.domain.usecases.base.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Inject

class UpdateComicsAction @Inject constructor(
    private val comicsDataRepository: ComicsDataRepository,
    @Background backgroundScheduler: Scheduler,
    @Foreground foregroundScheduler: Scheduler
) : CompletableUseCase<ComicsEntity>(
    backgroundScheduler,
    foregroundScheduler
) {

    override fun generateCompletable(input: ComicsEntity?): Completable {
        if (input == null) {
            throw IllegalArgumentException("UpdateComicsAction parameter can't be null")
        }
        return comicsDataRepository.updateComics(input)
    }
}