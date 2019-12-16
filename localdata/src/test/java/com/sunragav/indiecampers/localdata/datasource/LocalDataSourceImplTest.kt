package com.sunragav.indiecampers.localdata.datasource

import com.sunragav.indiecampers.home.data.repository.Callback
import com.sunragav.indiecampers.localdata.datasource.utils.TestLocalDataContainer.Companion.getComicsList
import com.sunragav.indiecampers.localdata.db.ComicsListDAO
import com.sunragav.indiecampers.localdata.db.FavoriteComicsDAO
import com.sunragav.indiecampers.localdata.mapper.ComicsFavoritesMapper
import com.sunragav.indiecampers.localdata.mapper.ComicsLocalMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplTest {
    private lateinit var localDataSourceImpl: LocalDataSourceImpl

    private var comicsListDAO: ComicsListDAO = mockk()
    private var favoritesDAO: FavoriteComicsDAO = mockk()
    private var comicsFavoritesMapper = ComicsFavoritesMapper()
    private var comicsLocalMapper = ComicsLocalMapper()
    @Before
    fun setup() {
        localDataSourceImpl = LocalDataSourceImpl(
            comicsLocalMapper,
            comicsFavoritesMapper,
            comicsListDAO,
            favoritesDAO
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
        val callBack: Callback = mockk()
        every { comicsListDAO.insert(any()) } throws (Throwable(message))
        localDataSourceImpl.insert(
            getComicsList().map { comicsLocalMapper.from(it) },
            callBack = callBack
        ).test()
            .assertSubscribed()
            .assertError { it.message == message }
        verify(exactly = 0) { callBack.invoke() }
    }

    @Test
    fun test_getComicsById_success() {
        val comicsList = getComicsList()
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        every { comicsListDAO.getComicsById(any()) } returns Observable.just(comicsList[0])
        localDataSourceImpl.getComicsById("123").test()
            .assertSubscribed()
            .assertValueCount(1)
            .assertValue { it == comicsEntityList[0] }
    }

    @Test
    fun test_getComicsById_failure() {
        every { comicsListDAO.getComicsById(any()) } returns Observable.empty()
        localDataSourceImpl.getComicsById("123").test()
            .assertSubscribed()
            .assertNoValues()
    }


    @Test
    fun test_update_success_case1() {
        //flagged=true flow-> fav.insert(), comDoa.update()
        //fav.delete() is not called
        val comicsList = getComicsList()
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        val callBack: Callback = mockk()
        val errorCallback: Callback = mockk()
        every { callBack.invoke() } returns Unit
        every { errorCallback.invoke() } returns Unit

        every { favoritesDAO.insert(any()) } returns Completable.complete()
        every { favoritesDAO.deleteFavorite(any()) } returns Completable.complete()
        every { comicsListDAO.update(any()) } returns Completable.complete()
        val flaggedComics = comicsEntityList[0].copy(flagged = true)
        localDataSourceImpl.update(
            comicsEntity = flaggedComics,
            callBack = callBack,
            errorCallback = errorCallback
        ).test()
        verify(exactly = 1) { favoritesDAO.insert(listOf(comicsFavoritesMapper.to(flaggedComics))) }
        verify(exactly = 1) { comicsListDAO.update(comicsLocalMapper.to(flaggedComics)) }
        verify(exactly = 0) { favoritesDAO.deleteFavorite(flaggedComics.id) }
        verify(exactly = 1) { callBack.invoke() }
        verify(exactly = 0) { errorCallback.invoke() }
    }

    @Test
    fun test_update_success_case2() {
        //flagged=false flow-> fav.delete(), comDoa.update()
        //fav.insert() is not called
        val comicsList = getComicsList()
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        val callBack: Callback = mockk()
        val errorCallback: Callback = mockk()
        every { callBack.invoke() } returns Unit
        every { errorCallback.invoke() } returns Unit

        every { favoritesDAO.insert(any()) } returns Completable.complete()
        every { favoritesDAO.deleteFavorite(any()) } returns Completable.complete()
        every { comicsListDAO.update(any()) } returns Completable.complete()

        val unFlaggedComics = comicsEntityList[0].copy(flagged = false)
        localDataSourceImpl.update(
            comicsEntity = unFlaggedComics,
            callBack = callBack,
            errorCallback = errorCallback
        ).test()
        verify(exactly = 0) { favoritesDAO.insert(listOf(comicsFavoritesMapper.to(unFlaggedComics))) }
        verify(exactly = 1) { favoritesDAO.deleteFavorite(unFlaggedComics.id) }
        verify(exactly = 1) { comicsListDAO.update(comicsLocalMapper.to(unFlaggedComics)) }
        verify(exactly = 1) { callBack.invoke() }
        verify(exactly = 0) { errorCallback.invoke() }
    }

    @Test
    fun test_update_error_case1() {
        //flagged=true flow-> fav.insert(), comDoa.update()
        val comicsList = getComicsList()
        val callBack: Callback = mockk()
        val errorCallback: Callback = mockk()
        every { callBack.invoke() } returns Unit
        every { errorCallback.invoke() } returns Unit
        val message = "Error"
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        every { favoritesDAO.insert(any()) } returns Completable.error(Exception(message))
        every { comicsListDAO.update(any()) } returns Completable.complete()
        val flaggedComics = comicsEntityList[0].copy(flagged = true)
        localDataSourceImpl.update(
            comicsEntity = flaggedComics,
            callBack = callBack,
            errorCallback = errorCallback
        ).test()
            .assertSubscribed()
            .assertNoErrors()
        verify(exactly = 0) { callBack.invoke() }
        verify(exactly = 1) { errorCallback.invoke() }

    }

    @Test
    fun test_update_error_case2() {
        //flagged=false flow-> fav.delete(), comDoa.update()
        val comicsList = getComicsList()
        val callBack: Callback = mockk()
        val errorCallback: Callback = mockk()
        every { callBack.invoke() } returns Unit
        every { errorCallback.invoke() } returns Unit
        val message = "Error"
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        every { favoritesDAO.deleteFavorite(any()) } returns Completable.error(Exception(message))
        every { comicsListDAO.update(any()) } returns Completable.error(Exception(message))
        val flaggedComics = comicsEntityList[0].copy(flagged = true)
        localDataSourceImpl.update(
            comicsEntity = flaggedComics.copy(flagged = false),
            callBack = callBack,
            errorCallback = errorCallback
        ).test()
            .assertSubscribed()
            .assertNoErrors()
        verify(exactly = 0) { callBack.invoke() }
        verify(exactly = 1) { errorCallback.invoke() }
    }

    @Test
    fun test_update_error_case3() {
        //flagged=true flow-> fav.delete() throws error, so comDoa.update() is not called and error callback is called
        val comicsList = getComicsList()
        val callBack: Callback = mockk()
        val errorCallback: Callback = mockk()
        every { callBack.invoke() } returns Unit
        every { errorCallback.invoke() } returns Unit
        val message = "Error"
        val comicsEntityList = comicsList.map { comicsLocalMapper.from(it) }
        every { favoritesDAO.insert(any()) } returns Completable.complete()
        every { comicsListDAO.update(any()) } returns Completable.error(Exception(message))
        val flaggedComics = comicsEntityList[0].copy(flagged = true)
        localDataSourceImpl.update(
            comicsEntity = flaggedComics.copy(flagged = true),
            callBack = callBack,
            errorCallback = errorCallback
        ).test()
            .assertSubscribed()
            .assertNoErrors()
        verify(exactly = 0) { callBack.invoke() }
        verify(exactly = 1) { errorCallback.invoke() }
    }
}