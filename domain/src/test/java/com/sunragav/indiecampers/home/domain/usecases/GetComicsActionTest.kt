package com.sunragav.indiecampers.home.domain.usecases

import com.sunragav.indiecampers.home.domain.repositories.ComicsRepository
import com.sunragav.indiecampers.home.domain.utils.TestDataContainer
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetComicsActionTest {

    companion object {
        const val searchKey = "123"
    }

    private lateinit var getComicsAction: GetComicsAction
    private val comicsRepository: ComicsRepository = mockk()

    @Before
    fun setup() {
        getComicsAction = GetComicsAction(
            comicsRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun test_getComicsAction_success() {

        val comics = TestDataContainer.getComics()

        every { comicsRepository.getComics(searchKey) }
            .returns(Observable.just(comics))

        val testObserver = getComicsAction.buildUseCase(
            searchKey
        ).test()

        testObserver
            .assertSubscribed()
            .assertValue { it == comics }
    }

    @Test
    fun test_getComicsAction_error() {
        val errorMsg = "ERROR OCCURRED"

        every { comicsRepository.getComics(searchKey) }
            .returns(Observable.error(Throwable(errorMsg)))

        val testObserver = getComicsAction.buildUseCase(
            searchKey
        ).test()

        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(errorMsg, false) ?: false }
            .assertNotComplete()
    }
}