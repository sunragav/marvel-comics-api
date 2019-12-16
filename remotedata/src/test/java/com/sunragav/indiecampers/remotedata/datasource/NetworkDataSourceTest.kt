package com.sunragav.indiecampers.remotedata.datasource

import com.sunragav.indiecampers.home.domain.entities.ComicsEntity
import com.sunragav.indiecampers.remotedata.api.ComicsService
import com.sunragav.indiecampers.remotedata.datasource.utils.TestRemoteDataContainer.Companion.getComic
import com.sunragav.indiecampers.remotedata.datasource.utils.TestRemoteDataContainer.Companion.getComicsList
import com.sunragav.indiecampers.remotedata.datasource.utils.TestRemoteDataContainer.Companion.getDataWrapper
import com.sunragav.indiecampers.remotedata.mapper.ComicsRemoteMapper
import com.sunragav.indiecampers.utils.HashGeneratorImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NetworkDataSourceTest {

    var comicsService: ComicsService = mockk()
    private val comicsRemoteMapper = ComicsRemoteMapper()
    private lateinit var networkDatasource: NetworkDataSource
    private val hashGenerator = HashGeneratorImpl()
    @Before
    fun setUp() {
        networkDatasource = NetworkDataSource(comicsService, comicsRemoteMapper, hashGenerator)
    }

    @Test
    fun test_getComicsList_success() {
        val dataWrapper = getDataWrapper()
        val comicsList = getComicsList()
        var result: List<ComicsEntity>? = null
        var error: Throwable? = null
        every {
            comicsService.getComicsList(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns (Single.just(dataWrapper))

        networkDatasource.getComicsList("id", 0, 20, { result = it }) { error = it }

        verify { comicsService.getComicsList(any(), any(), any(), any(), any()) }
        assertThat(
            result?.zip(comicsList)?.all { it.first == comicsRemoteMapper.from(it.second) },
            equalTo(true)
        )
        assertNull(error)
    }

    @Test
    fun test_getComicsList_failure() {
        val exception = Throwable("Network Error!!")
        var result: List<ComicsEntity>? = null
        var error: Throwable? = null
        every {
            comicsService.getComicsList(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns (Single.error(exception))

        networkDatasource.getComicsList("id", 0, 20, { result = it }) { error = it }

        verify { comicsService.getComicsList(any(), any(), any(), any(), any()) }
        assertNotNull(error)
        assertNull(result)
    }


    @Test
    fun test_getComicsById_success() {
        val comic = getComic()
        every {
            comicsService.getComicsById(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns (Observable.just(getDataWrapper(listOf(comic))))

        networkDatasource.getComicsById("some key").test()
            .assertSubscribed()
            .assertValue { it == comicsRemoteMapper.from(comic) }
            .assertNoErrors()
            .assertComplete()

    }

    @Test
    fun test_getComicsById_error() {
        val message = "Error"
        every {
            comicsService.getComicsById(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns (Observable.error(Throwable(message)))

        networkDatasource.getComicsById("some key").test()
            .assertSubscribed()
            .assertError { it.message == message }
            .assertNotComplete()

    }
}