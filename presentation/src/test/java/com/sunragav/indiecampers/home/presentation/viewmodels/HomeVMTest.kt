package com.sunragav.indiecampers.home.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sunragav.indiecampers.home.domain.entities.NetworkStateRelay
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import com.sunragav.indiecampers.home.presentation.mapper.ComicsEntityMapper
import com.sunragav.indiecampers.home.presentation.utils.PagingDataSourceUtil
import com.sunragav.indiecampers.home.presentation.utils.TestDataContainer
import com.sunragav.indiecampers.home.presentation.utils.observeOnce
import io.mockk.every
import io.mockk.mockk
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val comicsDataRepository: ComicsDataRepository = mockk()

    private lateinit var comicsListHomeVM: HomeVM
    private val comicsMapper = ComicsEntityMapper()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setup() {

        val getComicsListAction = getComicsListAction()
        Dispatchers.setMain(mainThreadSurrogate)


        comicsListHomeVM = HomeVM(
            getComicsListAction
        )

        val result =
            GetComicsListActionResult(
                mockk(),
                mockk()
            )

        every {
            comicsDataRepository.getComicsList(
                any()
            )
        }.returns(result)

    }

    @Test
    fun test_comicsList() {
        val comicsList = TestDataContainer.getComicsList().map { comicsMapper.from(it) }
        val ds = PagingDataSourceUtil.createMockDataSourceFactory(comicsList)
        val actionResult = GetComicsListActionResult(ds, mockk())

        every { comicsDataRepository.getComicsList(any()) } returns (actionResult)

        comicsListHomeVM.comicsListSource.observeOnce {
            assertThat(it.snapshot(), equalTo(comicsList))
        }

        comicsListHomeVM.search("112")


    }

    private fun getComicsListAction(): GetComicsListAction {
        return GetComicsListAction(
            comicsDataRepository
        )
    }

}