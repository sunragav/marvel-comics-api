package com.sunragav.indiecampers.home.domain.usecases

import com.sunragav.indiecampers.home.domain.repositories.ComicsDataRepository
import io.mockk.mockk
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
    }

    private lateinit var getComicsListAction: GetComicsListAction

    @Mock
    lateinit var comicsDataRepository: ComicsDataRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        getComicsListAction = GetComicsListAction(
            comicsDataRepository
        )
    }

    @Test
    fun test_getComicsListAction_Success() {

        val result =
            GetComicsListAction.GetComicsListActionResult(mockk(), mockk())

        Mockito.`when`(
            comicsDataRepository.getComicsList(
                query
            )
        ).thenReturn(result)


        getComicsListAction.getComicsListActionResult(
            query
        )


        verify(comicsDataRepository, times(1)).getComicsList(query)


    }

}

