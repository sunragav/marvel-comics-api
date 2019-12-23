package com.sunragav.indiecampers.localdata.datasource

import com.sunragav.indiecampers.localdata.datasource.utils.TestLocalDataContainer.Companion.getComicsList
import com.sunragav.indiecampers.localdata.db.ComicsListDAO
import com.sunragav.indiecampers.localdata.mapper.ComicsLocalMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplTest {
    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    private var comicsListDAO: ComicsListDAO = mockk()
    private var comicsLocalMapper = ComicsLocalMapper()
    @Before
    fun setup() {
        localDataSourceImpl = LocalDataSourceImpl(
            comicsLocalMapper,
            comicsListDAO
        )
    }

    @Test
    fun test_insert_success() {
        val comicsList = getComicsList()
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        every { comicsListDAO.insert(comicsList) } returns Completable.complete()
        localDataSourceImpl.insert(comicsEntityList).test()
        verify(exactly = 1) { comicsListDAO.insert(comicsList) }
    }

    @Test
    fun test_insert_error() {
        val message = "Error"
        every { comicsListDAO.insert(any()) } returns Completable.error(Throwable(message))
        localDataSourceImpl.insert(
            getComicsList().map { comicsLocalMapper.from(it) }
        ).test()
            .assertSubscribed()
            .assertError { it.message == message }
    }
}