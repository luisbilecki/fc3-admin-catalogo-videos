package com.fullcycle.admin.catalog.application.genre.create

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean

@IntegrationTest
class CreateGenreUseCaseIT @Autowired constructor(
    private val useCase: CreateGenreUseCase,
    @SpyBean private val categoryGateway: CategoryGateway,
    @SpyBean private val genreGateway: GenreGateway,
    private val genreRepository: GenreRepository
) {
    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        val movies = categoryGateway!!.create(Category.newCategory("Filmes", null, true))
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(movies!!.id)
        val command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories))

        val actualOutput = useCase!!.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        val actualGenre = genreRepository.findById(actualOutput.id).get()
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            expectedCategories.size == actualGenre.getCategoryIDs().size
                    && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        )
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertNotNull(actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories))

        val actualOutput = useCase!!.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        val actualGenre = genreRepository!!.findById(actualOutput.id).get()
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            (
                    expectedCategories.size == actualGenre.getCategoryIDs().size
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
                    )
        )
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertNotNull(actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf<CategoryID>()
        val command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories))

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        val actualGenre = genreRepository!!.findById(actualOutput.id).get()
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertTrue(
            (
                    expectedCategories.size == actualGenre.getCategoryIDs().size
                            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
                    )
        )
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertNotNull(actualGenre.updatedAt)
        Assertions.assertNotNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        val expectedName = " "
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1
        val command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(NotificationException::class.java) {
            useCase.execute(
                command
            )
        }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(categoryGateway, times(0))!!.existsByIds(any())
        Mockito.verify(genreGateway, times(0))!!.create(any())
    }

    @Test
    fun givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        val expectedName: String? = null
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1
        val command = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(NotificationException::class.java) {
            useCase.execute(
                command
            )
        }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(categoryGateway, times(0))!!.existsByIds(any())
        Mockito.verify(genreGateway, times(0))!!.create(any())
    }

    @Test
    fun givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        val series = categoryGateway.create(Category.newCategory("Séries", null, true))
        val moviesCategory = CategoryID.from("456")
        val documentariesCategory = CategoryID.from("789")
        val expectName = " "
        val expectedIsActive = true
        val expectedCategories = listOf(moviesCategory, series!!.id, documentariesCategory)
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be empty"
        val expectedErrorCount = 2
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(NotificationException::class.java) {
            useCase.execute(
                command
            )
        }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors[0].message)
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors[1].message)
        Mockito.verify(categoryGateway, times(1))!!.existsByIds(any())
        Mockito.verify(genreGateway, times(0))!!.create(any())
    }

    private fun asString(categories: List<CategoryID>): List<String> {
        return categories.stream()
            .map(CategoryID::value)
            .toList()
    }
}