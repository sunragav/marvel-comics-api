package com.sunragav.indiecampers.home.data.repository

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import utils.TestDataContainer.Companion.getComics

class ComicsDataRepositoryImplTest {
    companion object {
        const val SEARCH_KEY = "123"
        const val ERROR = "Error msg"
    }

    private lateinit var comicsDataRepositoryImpl: ComicsDataRepositoryImpl

    private val localRepository: LocalRepository = mockk()
    private val remoteRepository: RemoteRepository = mockk()


    @Before
    fun setup() {
        comicsDataRepositoryImpl =
            ComicsDataRepositoryImpl(mockk(), localRepository, remoteRepository, mockk(), mockk())
    }

    @Test
    fun test_getComics_success() {
        val comics = getComics()
        every {
            localRepository.getComicsById(any())
        }.returns(Observable.just(comics))
        every {
            remoteRepository.getComicsById(any())
        }.returns(Observable.just(comics))

        val comicsObservable = comicsDataRepositoryImpl.getComics(SEARCH_KEY)
        val testObserver = comicsObservable.test()
        testObserver
            .assertSubscribed()
            .assertValue { it == comics }
            .assertValueCount(1)
    }


    @Test
    fun test_getComics_error() {
        val comics = getComics()

        //Should fail when local db errors
        every {
            localRepository.getComicsById(any())
        } returns (Observable.error(Throwable(ERROR)))
        every {
            remoteRepository.getComicsById(any())
        }.returns(Observable.just(comics))
        val comicsObservable = comicsDataRepositoryImpl.getComics(SEARCH_KEY)

        var testObserver = comicsObservable.test()
        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(ERROR, false) ?: false }
            .assertNotComplete()


        //Should not fail when remote service errors but local db has value, because
        //when local db has data there is no need for the remote service call
        every {
            localRepository.getComicsById(any())
        } returns (Observable.just(comics))
        every {
            remoteRepository.getComicsById(any())
        } returns (Observable.error(Throwable(ERROR)))

        testObserver = comicsDataRepositoryImpl.getComics(SEARCH_KEY).test()
        testObserver
            .assertSubscribed()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun test_updateComics_success() {

        every {
            localRepository.update(any())
        }.returns(Completable.complete())

        val comicsObservable = comicsDataRepositoryImpl.updateComics(mockk())
        val testObserver = comicsObservable.test()
        testObserver
            .assertSubscribed()
            .assertComplete()
    }


    @Test
    fun test_updateComics_error() {
        every {
            localRepository.update(any())
        } returns (Completable.error(Throwable(ERROR)))
        val comicsObservable = comicsDataRepositoryImpl.updateComics(mockk())

        val testObserver = comicsObservable.test()
        testObserver
            .assertSubscribed()
            .assertError { it.message?.equals(ERROR, false) ?: false }
            .assertNotComplete()
    }
}