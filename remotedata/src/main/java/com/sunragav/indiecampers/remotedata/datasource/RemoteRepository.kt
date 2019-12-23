package com.sunragav.indiecampers.remotedata.datasource

import com.sunragav.indiecampers.home.data.repository.RemoteRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.remotedata.api.ComicsService
import com.sunragav.indiecampers.remotedata.mapper.ComicsRemoteMapper
import com.sunragav.indiecampers.remotedata.models.Comic
import com.sunragav.indiecampers.remotedata.models.DataWrapper
import com.sunragav.indiecampers.remotedata.qualifiers.PrivateKey
import com.sunragav.indiecampers.remotedata.qualifiers.PublicKey
import com.sunragav.indiecampers.utils.HashGenerator
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val comicsService: ComicsService,
    private val comicsRemoteMapper: ComicsRemoteMapper,
    private val hashGenerator: HashGenerator,
    @PublicKey private val publicKey: String,
    @PrivateKey private val privateKey: String,
    @Background private val backgroundThread: Scheduler

) : RemoteRepository {
    override fun getComicsList(
        query: String,
        lastRequestedPage: Int,
        limit: Int
    ): Single<List<ComicsEntity>> {
        val timestamp = System.currentTimeMillis()
        val hash = "$timestamp$privateKey$publicKey"
        return  comicsService.getComicsListStartsWithTitle(
            titleStartsWith = query,
            offset = lastRequestedPage,
            limit = limit,
            timestamp = timestamp,
            md5Digest = hashGenerator.buildMD5Digest(hash)
        ).subscribeOn(backgroundThread)
            .map {
                it.data.results.map { comic ->
                    comicsRemoteMapper.from(
                        comic
                    )
                }
            }

    }
}