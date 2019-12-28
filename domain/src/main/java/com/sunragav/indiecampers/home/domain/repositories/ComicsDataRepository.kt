package com.sunragav.indiecampers.home.domain.repositories

import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import io.reactivex.Completable

interface ComicsDataRepository {

    fun getComicsList(
        query: GetComicsListAction.Params
    ): GetComicsListActionResult

    fun clean()

}