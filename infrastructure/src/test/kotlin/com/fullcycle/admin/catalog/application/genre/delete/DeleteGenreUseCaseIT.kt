package com.fullcycle.admin.catalog.application.genre.delete

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class DeleteGenreUseCaseIT @Autowired constructor(
    private val useCase: DeleteGenreUseCase,
    private val genreGateway: GenreGateway,
    private val genreRepository: GenreRepository
) {

    @Test
    fun givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        val genre = genreGateway.create(Genre.newGenre("Ação", true))
        val expectedId = genre.id

        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertDoesNotThrow { useCase!!.execute(expectedId.value) }
        Assertions.assertEquals(0, genreRepository.count())
    }

    @Test
    fun givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        genreGateway.create(Genre.newGenre("Ação", true))
        val expectedId = GenreID.from("123")

        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertDoesNotThrow { useCase.execute(expectedId.value) }
        Assertions.assertEquals(1, genreRepository.count())
    }
}