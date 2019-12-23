package com.sunragav.indiecampers.home.domain.repositories

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import io.reactivex.Completable
import io.reactivex.Observable

interface ComicsDataRepository {

    fun getComicsList(
        query: GetComicsListAction.Params
    ): GetComicsListActionResult

}