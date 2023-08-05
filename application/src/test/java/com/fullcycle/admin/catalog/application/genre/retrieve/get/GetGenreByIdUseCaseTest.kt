package com.fullcycle.admin.catalog.application.genre.retrieve.get

import com.fullcycle.admin.catalog.application.UseCaseTest
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times

class GetGenreByIdUseCaseTest : UseCaseTest() {

    @InjectMocks
    private lateinit var useCase: DefaultGetGenreByIdUseCase

    @Mock
    private lateinit var genreGateway: GenreGateway

    override val mocks: List<Any?>
       get() = listOf(genreGateway)

    @Test
    fun givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )
        val genre = Genre.newGenre(expectedName, expectedIsActive)
            .addCategories(expectedCategories)
        val expectedId = genre.id
        `when`(genreGateway.findById(any()))
            .thenReturn(genre)

        val actualGenre = useCase.execute(expectedId.value)

        Assertions.assertEquals(expectedId.value, actualGenre.id())
        Assertions.assertEquals(expectedName, actualGenre.name())
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive())
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories())
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt())
        Assertions.assertEquals(genre.updatedAt, actualGenre.updatedAt())
        Assertions.assertEquals(genre.deletedAt, actualGenre.deletedAt())
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
    }

    @Test
    fun givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        val expectedErrorMessage = "Genre with ID 123 was not found"
        val expectedId = GenreID.from("123")
        `when`(genreGateway.findById(eq(expectedId)))
            .thenReturn(null)

        val actualException = Assertions.assertThrows(
            NotFoundException::class.java
        ) { useCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    private fun asString(ids: List<CategoryID>): List<String> {
        return ids.stream()
            .map(CategoryID::value)
            .toList()
    }
}