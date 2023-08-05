package com.fullcycle.admin.catalog.application.genre.create

import com.fullcycle.admin.catalog.application.UseCaseTest
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.times
import java.util.*

class CreateGenreUseCaseTest : UseCaseTest() {

    @InjectMocks
    private lateinit var useCase: DefaultCreateGenreUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @Mock
    private lateinit var genreGateway: GenreGateway

    override val mocks: List<Any?>
        get() = listOf(categoryGateway, genreGateway)

    @Test
    fun givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))
        `when`(genreGateway!!.create(any<Genre>()))
            .thenAnswer(returnsFirstArg<Genre>())

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        Mockito.verify(genreGateway, times(1)).create(argThat { genre ->
            (expectName == genre.name && expectedIsActive == genre.isActive && expectedCategories == genre.categories
                    && Objects.nonNull(genre.id)
                    && Objects.nonNull(genre.createdAt)
                    && Objects.nonNull(genre.updatedAt)
                    && Objects.isNull(genre.deletedAt))
        })
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))
        `when`(categoryGateway.existsByIds(any()))
            .thenReturn(expectedCategories)
        `when`(genreGateway.create(any()))
            .thenAnswer(returnsFirstArg<Genre>())

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories)
        Mockito.verify(genreGateway, times(1)).create(argThat { aGenre ->
            (expectName == aGenre.name && expectedIsActive == aGenre.isActive && expectedCategories == aGenre.categories
                    && Objects.nonNull(aGenre.id)
                    && Objects.nonNull(aGenre.createdAt)
                    && Objects.nonNull(aGenre.updatedAt))
                    && Objects.isNull(aGenre.deletedAt)
        })
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        val expectName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf<CategoryID>()
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))
        `when`(genreGateway.create(any()))
            .thenAnswer(returnsFirstArg<Genre>())

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        Mockito.verify(genreGateway, times(1)).create(argThat { aGenre ->
            (expectName == aGenre.name && expectedIsActive == aGenre.isActive && expectedCategories == aGenre.categories
                    && Objects.nonNull(aGenre.id)
                    && Objects.nonNull(aGenre.createdAt)
                    && Objects.nonNull(aGenre.updatedAt)
                    && Objects.nonNull(aGenre.deletedAt))
        })
    }

    @Test
    fun givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        val expectName: String? = " "
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { useCase.execute(command) }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(categoryGateway, times(0)).existsByIds(any())
        Mockito.verify(genreGateway, times(0)).create(any())
    }

    @Test
    fun givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        val expectName: String? = null
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { useCase.execute(command) }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(categoryGateway, times(0)).existsByIds(any())
        Mockito.verify(genreGateway, times(0)).create(any())
    }

    @Test
    fun givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        val movies = CategoryID.from("456")
        val series = CategoryID.from("123")
        val documentaries = CategoryID.from("789")
        val expectName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(movies, series, documentaries)
        val expectedErrorMessage = "Some categories could not be found: 456, 789"
        val expectedErrorCount = 1
        `when`(categoryGateway.existsByIds(any()))
            .thenReturn(listOf(series))
        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { useCase.execute(command) }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(categoryGateway, times(1)).existsByIds(any())
        Mockito.verify(genreGateway, times(0)).create(any())
    }

    @Test
    fun givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        val movies = CategoryID.from("456")
        val series = CategoryID.from("123")
        val documentaries = CategoryID.from("789")
        val expectName = " "
        val expectedIsActive = true
        val expectedCategories = listOf(movies, series, documentaries)
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be empty"
        val expectedErrorCount = 2

        `when`(categoryGateway.existsByIds(any()))
            .thenReturn(listOf(series))

        val command = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories))

        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { useCase.execute(command) }

        Assertions.assertNotNull(actualException)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors[0].message)
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors[1].message)
        Mockito.verify(categoryGateway, times(1)).existsByIds(any())
        Mockito.verify(genreGateway, times(0)).create(any())
    }

    private fun asString(categories: List<CategoryID>): List<String> {
        return categories.stream()
            .map(CategoryID::value)
            .toList()
    }
}