package com.sunragav.indiecampers.home.domain.usecases

import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import javax.inject.Inject

class CleanAction @Inject constructor(
    private val comicsDataRepository: ComicsDataRepository
) {
    fun execute() {
        comicsDataRepository.clean()
    }
}