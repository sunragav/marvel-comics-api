package com.sunragav.indiecampers.remotedata.api

import com.sunragav.indiecampers.remotedata.models.Comic
import com.sunragav.indiecampers.remotedata.models.DataWrapper
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val HASH = "hash"
const val TIMESTAMP = "ts"
const val OFFSET = "offset"
const val LIMIT = "limit"
const val TITLE = "titleStartsWith"

interface ComicsService {

    @GET("comics")
    fun getComicsList(
        @Query(HASH) md5Digest: String,
        @Query(TIMESTAMP) timestamp: Long,
        @Query(OFFSET) offset: Int?,
        @Query(LIMIT) limit: Int?,
        @Query(TITLE) titleStartsWith: String
    ): Single<DataWrapper<List<Comic>>>


    @GET("comics/{comicsId}")
    fun getComicsById(
        @Path("comicsId") id: String,
        @Query(HASH) md5Digest: String,
        @Query(TIMESTAMP) timestamp: Long,
        @Query(OFFSET) offset: Int?,
        @Query(LIMIT) limit: Int?
    ): Observable<DataWrapper<List<Comic>>>
}