package com.sunragav.indiecampers.localdata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.sunragav.indiecampers.localdata.db.ComicsDB
import com.sunragav.indiecampers.localdata.db.FavoriteComicsDAO
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import utils.TestDataContainer

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class FavoritesDAOTest {
    companion object {
        private const val LIMIT = 20
    }

    private lateinit var comicsDB: ComicsDB
    private lateinit var favoriteListDAO: FavoriteComicsDAO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        comicsDB = Room.inMemoryDatabaseBuilder(context, ComicsDB::class.java)
            .allowMainThreadQueries()
            .build()

        favoriteListDAO = comicsDB.getFavoritesDao()
    }

    @After
    fun tearDown() {
        favoriteListDAO.clearFavorites()
        comicsDB.close()
    }

    @Test
    fun test_saveAndRetrieveComicsList() {

        val comicsList = TestDataContainer.getFavoritesList()
        val comicsCount = comicsList.size

        favoriteListDAO.insert(comicsList)

        val result =
            (favoriteListDAO.getFavoriteComicsList().create() as LimitOffsetDataSource).loadRange(
                0,
                LIMIT
            )
        assertThat(result.size, equalTo(comicsCount))
    }

    @Test
    fun test_deleteFavoriteComics() {

        val comics = TestDataContainer.getFavorite()

        favoriteListDAO.insert(listOf(comics))
        assertThat(comics.flagged, equalTo(true))

        favoriteListDAO.deleteFavorite(comics.id)


        val result =
            (favoriteListDAO.getFavoriteComicsList().create() as LimitOffsetDataSource).loadRange(
                0,
                LIMIT
            )

        assertThat(result.none { it.id == comics.id }, equalTo(true))

    }

    @Test
    fun test_clearComicsTable() {
        val comicsList = TestDataContainer.getFavoritesList()

        favoriteListDAO.insert(comicsList)

        var result =
            (favoriteListDAO.getFavoriteComicsList().create() as LimitOffsetDataSource).loadRange(
                0,
                LIMIT
            )
        assertThat(result.size, equalTo(comicsList.size))


        favoriteListDAO.clearFavorites()

        result =
            (favoriteListDAO.getFavoriteComicsList().create() as LimitOffsetDataSource).loadRange(
                0,
                LIMIT
            )
        assertThat(result.size, equalTo(0))

    }

}