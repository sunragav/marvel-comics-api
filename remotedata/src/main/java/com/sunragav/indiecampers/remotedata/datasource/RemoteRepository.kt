package com.sunragav.indiecampers.remotedata.datasource

import com.sunragav.indiecampers.home.data.repository.RemoteRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.remotedata.api.ComicsService
import com.sunragav.indiecampers.remotedata.mapper.ComicsRemoteMapper
import com.sunragav.indiecampers.remotedata.models.Comic
import com.sunragav.indiecampers.remotedata.models.DataWrapper
import com.sunragav.indiecampers.remotedata.qualifiers.PrivateKey
import com.sunragav.indiecampers.remotedata.qualifiers.PublicKey
import com.sunragav.indiecampers.utils.HashGenerator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val comicsService: ComicsService,
    private val comicsRemoteMapper: ComicsRemoteMapper,
    private val hashGenerator: HashGenerator,
    @PublicKey private val publicKey: String,
    @PrivateKey private val privatekey: String

) : RemoteRepository {
    override fun getComicsList(
        query: String,
        lastRequestedPage: Int,
        limit: Int
    ): Single<List<ComicsEntity>> {
        return serviceCall(
            query = query,
            lastRequestedPage = lastRequestedPage * limit,
            limit = limit
        ).subscribeOn(Schedulers.io())
            .map {
                it.data.results.map { comic ->
                    comicsRemoteMapper.from(
                        comic
                    )
                }
            }

    }

    private fun serviceCall(
        query: String,
        lastRequestedPage: Int,
        limit: Int
    ): Single<DataWrapper<List<Comic>>> {
        val timestamp = System.currentTimeMillis()
        val hash = "$timestamp$privatekey$publicKey"
        return (if (query.isBlank())
            comicsService.getAllComicsList(
                offset = lastRequestedPage,
                limit = limit,
                timestamp = timestamp,
                md5Digest = hashGenerator.buildMD5Digest(hash)
            )
        else comicsService.getComicsListStartsWithTitle(
            titleStartsWith = query,
            offset = lastRequestedPage,
            limit = limit,
            timestamp = timestamp,
            md5Digest = hashGenerator.buildMD5Digest(hash)
        ))
    }


    override fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity> {
        val timestamp = System.currentTimeMillis()
        val hash = "$timestamp$privatekey$publicKey"
        return comicsService.getComicsById(
            id = uniqueIdentifier,
            timestamp = timestamp,
            md5Digest = hash,
            offset = 0,
            limit = 1
        ).map { comicsRemoteMapper.from(it.data.results.first()) }
    }

    private fun isValid(comic: Comic) = with(comic) { id > 0 && title.isNotBlank() }
}