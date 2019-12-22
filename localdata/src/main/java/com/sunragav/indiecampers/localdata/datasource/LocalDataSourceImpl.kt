package com.sunragav.indiecampers.localdata.datasource

import androidx.paging.DataSource
import com.sunragav.indiecampers.home.data.repository.LocalRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.qualifiers.Background
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.localdata.db.ComicsListDAO
import com.sunragav.indiecampers.localdata.db.FavoriteComicsDAO
import com.sunragav.indiecampers.localdata.db.RequestTrackerDao
import com.sunragav.indiecampers.localdata.mapper.ComicsFavoritesMapper
import com.sunragav.indiecampers.localdata.mapper.ComicsLocalMapper
import com.sunragav.indiecampers.localdata.models.Request
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val comicsLocalMapper: ComicsLocalMapper,
    private val comicsFavoritesMapper: ComicsFavoritesMapper,
    private val comicsListDAO: ComicsListDAO,
    private val favoriteComicsDAO: FavoriteComicsDAO,
    private val requestDoa: RequestTrackerDao,
    @Background private val backgroundThread:Scheduler
) : LocalRepository {
    override fun insert(
        comicsEntityList: List<ComicsEntity>
    ): Completable {
        return comicsListDAO.insert(comicsEntityList.map { comicsLocalMapper.to(it) })
    }

    override fun getComicsListDatasourceFactory(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity> {
        return comicsListDAO.getComicsList(
            "%${param.searchKey.toUpperCase(Locale.getDefault()).replace(' ', '%')}%"
        ).map { comicsLocalMapper.from(it) }
    }

    override fun getComicsById(uniqueIdentifier: String): Observable<ComicsEntity> {
        return comicsListDAO.getComicsById(uniqueIdentifier).map { comicsLocalMapper.from(it) }
    }

    override fun getFavoriteComicsListDatasourceFactory(limit: Int): DataSource.Factory<Int, ComicsEntity> {
        return favoriteComicsDAO.getFavoriteComicsList().map { comicsFavoritesMapper.from(it) }
    }

    override fun update(
        comicsEntity: ComicsEntity
    ): Completable {
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

    override fun getPreviousRequest(): GetComicsListAction.Params {
        val request: Request =
            Observable.fromCallable { requestDoa.getRequest() }.subscribeOn(backgroundThread)
                .onErrorReturn {
                    Request(
                        id = 0,
                        searchKey = "",
                        flagged = false,
                        offset = 0
                    )
                }
                .blockingFirst()

        return with(request) {
            GetComicsListAction.Params(
                searchKey = searchKey,
                flagged = flagged,
                offset = offset
            )
        }

    }


    override fun updateRequest(param: GetComicsListAction.Params) {
        requestDoa.delete()
        with(param) {
            requestDoa.insert(
                Request(
                    id = 0,
                    searchKey = searchKey,
                    flagged = flagged,
                    offset = offset
                )
            )
        }
    }

}
