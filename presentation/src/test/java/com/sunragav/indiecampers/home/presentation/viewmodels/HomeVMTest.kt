package com.sunragav.indiecampers.home.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jakewharton.rxrelay2.BehaviorRelay
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.repositories.ComicsRepository
import com.sunragav.indiecampers.home.domain.usecases.GetComicsListAction
import com.sunragav.indiecampers.home.domain.usecases.UpdateComicsAction
import com.sunragav.indiecampers.home.presentation.mapper.ComicsEntityMapper
import io.mockk.every
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeVMTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val comicsRepository: ComicsRepository = mockk()

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
            GetComicsListAction.GetComicsListActionResult(
                mockk(),
                mockk(),
                domainNetworkState
            )

        every {
            comicsRepository.getComicsList(
                any()
            )
        }.returns(result)

    }

    @Test
    fun test_NetworkState() {
        val networkState = comicsListHomeVM.networkState
        val networkStateObserver = TestObserver.create<NetworkState>()
        networkState.subscribe(networkStateObserver)
        networkStateObserver.assertSubscribed()
        networkStateObserver.assertValue(NetworkState.EMPTY)

        val comicsListSource = comicsListHomeVM.comicsListSource

        comicsListSource.observeForever { /*Do Nothing*/ }
        comicsListHomeVM.search("112")
        testNetWorkState(NetworkState.LOADING, networkState)
        testNetWorkState(NetworkState.LOADED, networkState)
        testNetWorkState(NetworkState.ERROR, networkState)
    }

    private fun testNetWorkState(state: NetworkState, relay: BehaviorRelay<NetworkState>) {
        domainNetworkState.accept(state)
        assertThat(state, equalTo(relay.value))
    }

    private fun getComicsListAction(): GetComicsListAction {
        return GetComicsListAction(
            comicsRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    private fun getUpdateComicsAction(): UpdateComicsAction {
        return UpdateComicsAction(
            comicsRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }
}