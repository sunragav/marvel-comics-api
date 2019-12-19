package com.sunragav.indiecampers.home.data.repository

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import io.reactivex.Observable
import io.reactivex.Single

interface RemoteRepository {
    fun getComicsList(
        query: String, lastRequestedPage: Int, limit: Int
    ): Single<List<ComicsEntity>>

    fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity>
}