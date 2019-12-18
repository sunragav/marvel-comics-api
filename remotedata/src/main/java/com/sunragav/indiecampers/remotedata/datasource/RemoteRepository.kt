package com.sunragav.indiecampers.remotedata.datasource

import com.sunragav.indiecampers.home.data.repository.RemoteRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.remotedata.api.ComicsService
import com.sunragav.indiecampers.remotedata.mapper.ComicsRemoteMapper
import com.sunragav.indiecampers.remotedata.models.Comic
import com.sunragav.indiecampers.remotedata.qualifiers.PrivateKey
import com.sunragav.indiecampers.remotedata.qualifiers.PublicKey
import com.sunragav.indiecampers.utils.HashGenerator
import io.reactivex.Observable
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
        limit: Int,
        successCallback: (List<ComicsEntity>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        try {
            val timestamp = System.currentTimeMillis()
            successCallback.invoke(
                comicsService.getComicsList(
                    titleStartsWith = query,
                    offset = lastRequestedPage,
                    limit = limit,
                    timestamp = timestamp,
                    md5Digest = hashGenerator.buildMD5Digest("$timestamp$privatekey$publicKey")
                ).observeOn(Schedulers.io())
                    .blockingGet().data.results.filter(::isValid).map { comic ->
                    comicsRemoteMapper.from(
                        comic
                    )
                }
            )
        } catch (e: Throwable) {
            failureCallback.invoke(Throwable(e))
        }

    }

    override fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity> {
        return comicsService.getComicsById(
            id = uniqueIdentifier,
            timestamp = 4975897,
            md5Digest = "",
            offset = 0,
            limit = 1
        ).map { comicsRemoteMapper.from(it.data.results.first()) }
    }

    private fun isValid(comic: Comic) = with(comic) { id > 0 && title.isNotBlank() }
}