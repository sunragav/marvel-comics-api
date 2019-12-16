package com.sunragav.indiecampers.home.data.repository

import androidx.paging.DataSource
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import io.reactivex.Completable
import io.reactivex.Observable

typealias Callback = () -> Unit

interface LocalRepository {
    fun insert(comicsEntityList: List<ComicsEntity>, callBack: Callback? = null): Completable
    fun getComicsList(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity>
    fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity>
    fun getFavoriteComics(limit: Int): DataSource.Factory<Int, ComicsEntity>
    fun update(comicsEntity: ComicsEntity): Completable
}