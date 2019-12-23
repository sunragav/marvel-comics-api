package com.sunragav.indiecampers.home.data.repository

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import io.reactivex.Single

interface RemoteRepository {
    fun getComicsList(
        query: String, offset: Int, limit: Int
    ): Single<List<ComicsEntity>>

}