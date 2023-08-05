package com.fullcycle.admin.catalog.application.genre.delete

import com.fullcycle.admin.catalog.application.UseCaseTest
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.times

class DeleteGenreUseCaseTest : UseCaseTest() {
    @InjectMocks
    private lateinit var useCase: DefaultDeleteGenreUseCase

    @Mock
    private lateinit var genreGateway: GenreGateway
    override val mocks: List<Any?>
        get() = listOf(genreGateway)

    @Test
    fun givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        val genre = Genre.newGenre("Ação", true)
        val expectedId = genre.id
        doNothing()
            .`when`(genreGateway).deleteById(any())

          Assertions.assertDoesNotThrow {
            useCase.execute(
                expectedId.value
            )
        }

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId)
    }

    @Test
    fun givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        val expectedId = GenreID.from("123")
        doNothing()
            .`when`(genreGateway).deleteById(any())

        Assertions.assertDoesNotThrow {
            useCase.execute(
                expectedId.value
            )
        }

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId)
    }

    @Test
    fun givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        val genre = Genre.newGenre("Ação", true)
        val expectedId = genre.getId()
        doThrow(IllegalStateException("Gateway error"))
            .`when`(genreGateway).deleteById(any())

        Assertions.assertThrows(
            IllegalStateException::class.java
        ) { useCase.execute(expectedId.getValue()) }

        Mockito.verify(genreGateway, times(1)).deleteById(expectedId)
    }
}