package com.sunragav.indiecampers.home.domain.usecases

import com.jakewharton.rxrelay2.BehaviorRelay
import com.sunragav.indiecampers.home.domain.entities.NetworkState
import com.sunragav.indiecampers.home.domain.repositories.ComicsRepository
import io.mockk.mockk
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class GetComicsListActionTest {
    companion object {
        private val query = GetComicsListAction.Params(
            searchKey = "123",
            flagged = false
        )
        private const val ERROR = "Network Error occurred"
    }

    private lateinit var getComicsListAction: GetComicsListAction

    @Mock
    lateinit var comicsRepository: ComicsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getComicsListAction = GetComicsListAction(
            comicsRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun test_getComicsAction_Success() {
        val networkState: BehaviorRelay<NetworkState> = BehaviorRelay.create()

        val result =
            GetComicsListAction.GetComicsListActionResult(mockk(), mockk(), networkState)

        Mockito.`when`(
            comicsRepository.getComicsList(
                query
            )
        ).thenReturn(result)


        val testObserver = getComicsListAction.buildUseCase(
            query
        ).test()
        testObserver.assertSubscribed()
            .assertValue { it == result }
            .assertComplete()

        verify(comicsRepository, times(1)).getComicsList(query)
        networkState.accept(NetworkState.LOADED)
        val networkStateObserver = TestObserver.create<NetworkState>()
        networkState.subscribe(networkStateObserver)
        networkStateObserver.assertSubscribed()
        networkStateObserver.assertValue(NetworkState.LOADED)

    }

    @Test
    fun test_getComicsAction_Error() {
        val networkState: BehaviorRelay<NetworkState> = BehaviorRelay.create()
        networkState.accept(NetworkState.ERROR)
        val result =
            GetComicsListAction.GetComicsListActionResult(mockk(), mockk(), networkState)
        Mockito.`when`(
            comicsRepository.getComicsList(
                query
            )
        ).thenReturn(result)

        val testObserver = getComicsListAction.buildUseCase(
            query
        ).test()

        testObserver.assertSubscribed()
            .assertValue {
                it.networkState == networkState
            }
            .assertComplete()
        val networkStateObserver = TestObserver.create<NetworkState>()
        networkState.subscribe(networkStateObserver)
        networkStateObserver.assertSubscribed()
        networkStateObserver.assertValue(NetworkState.ERROR)
    }
}

