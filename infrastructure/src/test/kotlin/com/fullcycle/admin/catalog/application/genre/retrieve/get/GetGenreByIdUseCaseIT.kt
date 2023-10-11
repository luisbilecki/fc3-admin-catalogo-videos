package com.fullcycle.admin.catalog.application.genre.retrieve.get

import com.fullcycle.admin.catalog.IntegrationTest;
import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class GetGenreByIdUseCaseIT @Autowired constructor(
    private val useCase: GetGenreByIdUseCase,
    val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway
) {

    @Test
    fun givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        val seriesCategory = categoryGateway.create(Category.newCategory("Séries", null, true))
        val moviesCategory = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(seriesCategory!!.id, moviesCategory!!.id)
        val genre = genreGateway.create(
            Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories)
        )
        val expectedId = genre.id

        val actualGenre = useCase!!.execute(expectedId.value)

        Assertions.assertEquals(expectedId.value, actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            expectedCategories.size == actualGenre.categories.size
                    && asString(expectedCategories).containsAll(actualGenre.categories)
        )
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertEquals(genre.updatedAt, actualGenre.updatedAt)
        Assertions.assertEquals(genre.deletedAt, actualGenre.deletedAt)
    }

    @Test
    fun givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        val expectedErrorMessage = "Genre with ID 123 was not found"
        val expectedId = GenreID.from("123")

        val actualException = Assertions.assertThrows(NotFoundException::class.java) {
            useCase!!.execute(
                expectedId.value
            )
        }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    private fun asString(ids: List<CategoryID>): List<String> {
        return ids.stream()
            .map(CategoryID::value)
            .toList()
    }
}