package com.fullcycle.admin.catalog.e2e.category

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreCommand
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreUseCase
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean


@IntegrationTest
class UpdateGenreUseCaseIT @Autowired constructor(
    private val useCase: UpdateGenreUseCase,
    @SpyBean private val categoryGateway: CategoryGateway,
    @SpyBean private val genreGateway: GenreGateway,
    private val genreRepository: GenreRepository
) {

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        val genre = genreGateway.create(Genre.newGenre("acao", true))
        val expectedId = genre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        val actualOutput = useCase!!.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id)
        val actualGenre = genreRepository!!.findById(genre.id.value).get()
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            expectedCategories.size == actualGenre.getCategoryIDs().size
                    && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        )
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        val moviesCategory = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val seriesCategory = categoryGateway.create(Category.newCategory("Séries", null, true))
        val genre = genreGateway.create(Genre.newGenre("acao", true))
        val expectedId = genre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(moviesCategory!!.id, seriesCategory!!.id)
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        val actualOutput = useCase!!.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id)
        val actualGenre = genreRepository.findById(genre.id.value).get()
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            expectedCategories.size == actualGenre.getCategoryIDs().size
                    && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        )
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        val genre = genreGateway.create(Genre.newGenre("acao", true))
        val expectedId = genre.id
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf<CategoryID>()
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )
        Assertions.assertTrue(genre.isActive)
        Assertions.assertNull(genre.deletedAt)

        val actualOutput = useCase!!.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id)
        val actualGenre = genreRepository.findById(genre.id.value).get()
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            (
                    expectedCategories.size == actualGenre.getCategoryIDs().size
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
                    )
        )
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNotNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        val genre = genreGateway.create(Genre.newGenre("acao", true))
        val expectedId = genre.id
        val expectedName: String? = null
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be null"
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        val actualException: NotificationException = Assertions.assertThrows(NotificationException::class.java) {
            useCase.execute(
                command
            )
        }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(0)).existsByIds(any())
        Mockito.verify(genreGateway, times(0)).update(any())
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        val moviesCategory = categoryGateway.create(Category.newCategory("Filems", null, true))
        val seriesCategory = CategoryID.from("456")
        val documentariesCategory = CategoryID.from("789")
        val aGenre = genreGateway.create(Genre.newGenre("acao", true))
        val expectedId = aGenre.id
        val expectedName: String? = null
        val expectedIsActive = true
        val expectedCategories = listOf(moviesCategory!!.id, seriesCategory, documentariesCategory)
        val expectedErrorCount = 2
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be null"
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        val actualException = Assertions.assertThrows(NotificationException::class.java) {
            useCase.execute(
                command
            )
        }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors[0].message)
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors[1].message)
        Mockito.verify(genreGateway, times(1))!!.findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(1))!!.existsByIds(eq(expectedCategories))
        Mockito.verify(genreGateway, times(0))!!.update(any())
    }

    private fun asString(ids: List<CategoryID>): List<String> {
        return ids.stream()
            .map(CategoryID::value)
            .toList()
    }
}