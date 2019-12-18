package com.sunragav.indiecampers.home.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction.GetComicsListActionResult
import com.sunragav.indiecampers.home.domain.usecases.UpdateComicsAction
import com.sunragav.indiecampers.home.presentation.mapper.ComicsEntityMapper
import com.sunragav.indiecampers.home.presentation.utils.PagingDataSourceUtil
import com.sunragav.indiecampers.home.presentation.utils.TestDataContainer
import io.mockk.every
import io.mockk.mockk
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val comicsDataRepository: ComicsDataRepository = mockk()

    private lateinit var comicsListHomeVM: HomeVM
    private val comicsMapper = ComicsEntityMapper()
    private val domainNetworkState: BehaviorRelay<NetworkState> = BehaviorRelay.create()

    @Before
    fun setup() {

        val getComicsListAction = getComicsListAction()

        val updateComicsAction = getUpdateComicsAction()

        comicsListHomeVM = HomeVM(
            comicsMapper,
            getComicsListAction,
            updateComicsAction
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

        comicsListHomeVM.comicsListSource.observeForever { /*Do Nothing*/ }

        comicsListHomeVM.search("112")
        assertThat(comicsListHomeVM.comicsListSource.value?.snapshot(), equalTo(comicsList))

    }

    private fun getComicsListAction(): GetComicsListAction {
        return GetComicsListAction(
            comicsDataRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    private fun getUpdateComicsAction(): UpdateComicsAction {
        return UpdateComicsAction(
            comicsDataRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }
}