package com.sunragav.indiecampers.home.domain.usecases

import com.sunragav.indiecampers.home.domain.repositories.ComicsRepository
import com.sunragav.indiecampers.home.domain.utils.TestDataContainer
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class UpdateComicsActionTest {
    private lateinit var updateComicsAction: UpdateComicsAction

    @Mock
    private lateinit var comicsRepository: ComicsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        updateComicsAction = UpdateComicsAction(
            comicsRepository,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun test_updateComicsAction_success() {
        val comics = TestDataContainer.getComics()

        Mockito.`when`(comicsRepository.updateComics(comics))
            .thenReturn(Completable.complete())

        val testObserver = updateComicsAction.buildUseCase(
            comics
        ).test()

        verify(comicsRepository, times(1))
            .updateComics(comics)

        testObserver
            .assertSubscribed()
            .assertComplete()
    }

    @Test
    fun test_updateComicsAction_error() {
        val comics = TestDataContainer.getComics()
        val errorMsg = "ERROR OCCURRED"

        Mockito.`when`(comicsRepository.updateComics(comics))
            .thenReturn(Completable.error(Throwable(errorMsg)))

        val testObserver = updateComicsAction
            .buildUseCase(comics).test()

        verify(comicsRepository, times(1))
            .updateComics(comics)

        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(errorMsg) ?: false }
    }

    @Test(expected = IllegalArgumentException::class)
    fun test_updateComicsActionNoParameters_error() {
        val testObserver = updateComicsAction.buildUseCase().test()
        testObserver.assertSubscribed()
    }
}