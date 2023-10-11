package com.fullcycle.admin.catalog.application.genre.retrieve.list

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class ListGenreUseCaseIT @Autowired constructor(
    private val useCase: ListGenreUseCase,
    private val genreGateway: GenreGateway,
    private val genreRepository: GenreRepository
) {

    @Test
    fun givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        val genres = listOf(
            Genre.newGenre("Ação", true),
            Genre.newGenre("Aventura", true)
        )
        genreRepository.saveAllAndFlush(
            genres.stream()
                .map(GenreJpaEntity::from)
                .toList()
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
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualOutput = useCase.execute(query)

        Assertions.assertEquals(expectedPage, actualOutput.currentPage)
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage)
        Assertions.assertEquals(expectedTotal, actualOutput.total)
        Assertions.assertTrue(
            expectedItems.size === actualOutput.items.size
                    && expectedItems.containsAll(actualOutput.items)
        )
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
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualOutput = useCase.execute(query)

        Assertions.assertEquals(expectedPage, actualOutput.currentPage)
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage)
        Assertions.assertEquals(expectedTotal, actualOutput.total)
        Assertions.assertEquals(expectedItems, actualOutput.items)
    }
}