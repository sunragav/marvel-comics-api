package com.sunragav.indiecampers.home.domain.repositories

import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult

interface ComicsDataRepository {

    fun getComicsList(
        query: GetComicsListAction.Params
    ): GetComicsListActionResult

}