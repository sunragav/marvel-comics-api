package com.sunragav.indiecampers.feature_home.utils

import com.sunragav.indiecampers.remotedata.api.ComicsService
import com.sunragav.indiecampers.remotedata.models.Comic
import com.sunragav.indiecampers.remotedata.models.DataWrapper
import io.reactivex.Observable
import io.reactivex.Single


class FakeComicsService : ComicsService {
    override fun getComicsListStartsWithTitle(
        md5Digest: String,
        timestamp: Long,
        offset: Int?,
        limit: Int?,
        titleStartsWith: String
    ): Single<DataWrapper<List<Comic>>> {
        return Single.just(dataWrapper)
    }
}