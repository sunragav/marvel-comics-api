package com.sunragav.indiecampers.localdata.datasource

import androidx.paging.DataSource
import com.sunragav.indiecampers.home.data.repository.Callback
import com.sunragav.indiecampers.home.data.repository.LocalRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.localdata.db.ComicsListDAO
import com.sunragav.indiecampers.localdata.db.FavoriteComicsDAO
import com.sunragav.indiecampers.localdata.mapper.ComicsFavoritesMapper
import com.sunragav.indiecampers.localdata.mapper.ComicsLocalMapper
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val comicsLocalMapper: ComicsLocalMapper,
    private val comicsFavoritesMapper: ComicsFavoritesMapper,
    private val comicsListDAO: ComicsListDAO,
    private val favoriteComicsDAO: FavoriteComicsDAO
) : LocalRepository {
    override fun insert(comicsEntityList: List<ComicsEntity>, callBack: Callback?): Completable {
        return Completable.fromCallable {
            comicsListDAO.insert(comicsEntityList.map { comicsLocalMapper.to(it) })
            callBack?.invoke()
        }
    }

    override fun getComicsList(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity> {
        return (if (param.searchKey.isNotBlank())
            comicsListDAO.getComicsList(
                "%${param.searchKey.replace(' ', '%')}%",
                param.limit
            )
        else comicsListDAO.getComicsList(param.limit))
            .map { comicsLocalMapper.from(it) }
    }

    override fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity> {
        return comicsListDAO.getComicsById(uniqueIdentifier).map { comicsLocalMapper.from(it) }
    }

    override fun getFavoriteComics(limit: Int): DataSource.Factory<Int, ComicsEntity> {
        return favoriteComicsDAO.getFavoriteComicsList(limit).map { comicsFavoritesMapper.from(it) }
    }

    override fun update(comicsEntity: ComicsEntity): Completable {
        return Completable.fromCallable {
            val comicsLocal = comicsLocalMapper.to(comicsEntity)
            with(comicsLocal) {
                if (flagged) {
                    favoriteComicsDAO.insert(listOf(comicsFavoritesMapper.to(comicsEntity)))
                } else {
                    favoriteComicsDAO.deleteFavorite(id)
                }
                comicsListDAO.update(this)
            }
        }
    }

}
