package com.fullcycle.admin.catalog.application.genre.retrieve.list

import com.fullcycle.admin.catalog.application.UseCaseTest
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times

class ListGenreUseCaseTest : UseCaseTest() {

    @InjectMocks
    private lateinit var useCase: DefaultListGenreUseCase

    @Mock
    private lateinit var genreGateway: GenreGateway

    override val mocks: List<Any?>
        get() = listOf(genreGateway)

    @Test
    fun givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        val genres = listOf(
            Genre.newGenre("Ação", true),
            Genre.newGenre("Aventura", true)
        )
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "A"
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 2
        val expectedItems = genres.stream()
            .map(GenreListOutput::from)
            .toList()
        val expectedPagination = Pagination(
            expectedPage,
            expectedPerPage,
            expectedTotal.toLong(),
            genres
        )

        `when`(genreGateway!!.findAll(any()))
            .thenReturn(expectedPagination)

        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualOutput = useCase.execute(query)

        Assertions.assertEquals(expectedPage, actualOutput.currentPage)
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage)
        Assertions.assertEquals(expectedTotal, actualOutput.totalItems)
        Assertions.assertEquals(expectedItems, actualOutput.items)
        Mockito.verify(genreGateway, times(1)).findAll(eq(query))
    }

    @Test
    fun givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        val genres = listOf<Genre>()
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "A"
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedTotal = 0
        val expectedItems = listOf<GenreListOutput>()
        val expectedPagination = Pagination(
            expectedPage,
            expectedPerPage,
            expectedTotal.toLong(),
            genres
        )

        `when`(genreGateway.findAll(any()))
            .thenReturn(expectedPagination)

        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualOutput = useCase.execute(query)

        Assertions.assertEquals(expectedPage, actualOutput.currentPage)
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage)
        Assertions.assertEquals(expectedTotal, actualOutput.totalItems)
        Assertions.assertEquals(expectedItems, actualOutput.items)
        Mockito.verify(genreGateway, times(1)).findAll(eq(query))
    }
    @Test
    fun givenAValidQuery_whenCallsListGenreAndGatewayThrowsRandomError_shouldReturnException() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "A"
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway error"
        `when`(genreGateway.findAll(any()))
            .thenThrow(IllegalStateException(expectedErrorMessage))
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualOutput = Assertions.assertThrows(
            IllegalStateException::class.java
        ) { useCase.execute(query) }

        Assertions.assertEquals(expectedErrorMessage, actualOutput.message)
        Mockito.verify(genreGateway, times(1)).findAll(eq(query))
    }
}