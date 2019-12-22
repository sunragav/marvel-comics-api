package com.sunragav.indiecampers.home.domain.usecases

import androidx.paging.DataSource
import androidx.paging.PagedList
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import javax.inject.Inject

class GetComicsListAction @Inject constructor(
    private val comicsDataRepository: ComicsDataRepository
) {
    companion object {
        const val PAGE_SIZE = 20
    }


    data class Params(
        val searchKey: String = "",
        val flagged: Boolean = false,
        val limit: Int = PAGE_SIZE,
        val offset: Int = 0
    )

    fun getComicsListActionResult(input: Params): GetComicsListActionResult {
        return comicsDataRepository.getComicsList(input)
    }

    class GetComicsListActionResult(
        val dataSource: DataSource.Factory<Int, ComicsEntity>,
        val boundaryCallback: PagedList.BoundaryCallback<ComicsEntity>
    )


}