package com.sunragav.indiecampers.home.data.repository

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import io.reactivex.Observable

interface RemoteRepository {
    fun getComicsList(
        query: String, lastRequestedPage: Int, limit: Int,
        successCallback: (List<ComicsEntity>) -> Unit,
        failureCallback: (Throwable) -> Unit
    )

    fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity>
}