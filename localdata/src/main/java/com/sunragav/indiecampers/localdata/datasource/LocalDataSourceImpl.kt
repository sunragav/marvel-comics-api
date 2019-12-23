package com.sunragav.indiecampers.localdata.datasource

import androidx.paging.DataSource
import com.sunragav.indiecampers.home.data.repository.LocalRepository
import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.localdata.db.ComicsListDAO
import com.sunragav.indiecampers.localdata.mapper.ComicsLocalMapper
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val comicsLocalMapper: ComicsLocalMapper,
    private val comicsListDAO: ComicsListDAO
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
}
