package com.sunragav.indiecampers.localdata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.sunragav.indiecampers.localdata.db.ComicsDB
import com.sunragav.indiecampers.localdata.db.ComicsListDAO
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
class ComicsDAOTest {
    companion object {
        private const val LIMIT = 20
    }

    private lateinit var comicsDB: ComicsDB
    private lateinit var comicsListDAO: ComicsListDAO

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        comicsDB = Room.inMemoryDatabaseBuilder(context, ComicsDB::class.java)
            .allowMainThreadQueries()
            .build()

        comicsListDAO = comicsDB.getComicsListDao()
    }

    @After
    fun tearDown() {
        comicsListDAO.clearComicsFromTable()
        comicsDB.close()
    }

    @Test
    fun test_saveAndRetrieveComicsList() {

        val comicsList = TestDataContainer.getComicsList()
        val comicsCount = comicsList.size

        comicsListDAO.insert(comicsList).test()

        var result = (comicsListDAO.getComicsList(
            "%FAKE%"
        ).create() as LimitOffsetDataSource).loadRange(0, LIMIT)
        assertThat(result.size, equalTo(3))

        result = (comicsListDAO.getComicsList().create() as LimitOffsetDataSource).loadRange(
            0,
            LIMIT
        )
        assertThat(result.size, equalTo(comicsCount))

       comicsListDAO.getComicsById("143").test()
           .assertSubscribed()
           .assertValue{it==comicsList[0]}
           .assertNoErrors()
           .assertNotComplete() // As Room Observables are kept alive
    }

    @Test
    fun test_updateComics() {

        val comics = TestDataContainer.getComics()

        comicsListDAO.insert(listOf(comics)).test()
        assertThat(comics.flagged, equalTo(true))

        comicsListDAO.update(comics.copy(title = "TEST", flagged = false))



        comicsListDAO.getComicsById(comics.id)
            .test()
            .assertSubscribed()
            .assertValue {
                !it.flagged && it.title == "TEST"
            }
            .assertNotComplete() // As Room Observables are kept alive

    }

    @Test
    fun test_clearComicsTable() {
        val comicsList = TestDataContainer.getComicsList()

        comicsListDAO.insert(comicsList).subscribe()

        var result =
            (comicsListDAO.getComicsList().create() as LimitOffsetDataSource).loadRange(
                0,
                LIMIT
            )
        assertThat(result.size, equalTo(comicsList.size))


        comicsListDAO.clearComicsFromTable()

        result = (comicsListDAO.getComicsList().create() as LimitOffsetDataSource).loadRange(
            0,
            LIMIT
        )
        assertThat(result.size, equalTo(0))

    }

}