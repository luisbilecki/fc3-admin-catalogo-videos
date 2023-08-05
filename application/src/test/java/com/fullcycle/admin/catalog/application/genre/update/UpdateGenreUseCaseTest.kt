package com.fullcycle.admin.catalog.application.genre.update

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import java.util.*

@ExtendWith(MockitoExtension::class)
class UpdateGenreUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultUpdateGenreUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @Mock
    private lateinit var genreGateway: GenreGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        val genre = Genre.newGenre("acao", true)
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
        `when`(genreGateway.findById(any()))
            .thenReturn(Genre.with(genre))
        `when`(genreGateway.update(any()))
            .thenAnswer(returnsFirstArg<Genre?>())

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id)
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(genreGateway, times(1))
            .update(argThat { updatedGenre ->
                (expectedId == updatedGenre.id && expectedName == updatedGenre.name && expectedIsActive == updatedGenre.isActive
                        && expectedCategories == updatedGenre.categories && genre.createdAt == updatedGenre.createdAt
                        && genre.updatedAt.isBefore(updatedGenre.updatedAt)
                        && Objects.isNull(updatedGenre.deletedAt))
            })
    }

    @Test
    fun givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        val genre = Genre.newGenre("acao", true)
        val expectedId = genre.id
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(
            CategoryID.from("123"),
            CategoryID.from("456")
        )
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        `when`(genreGateway.findById(any()))
            .thenReturn(Genre.with(genre))
        `when`(categoryGateway.existsByIds(any()))
            .thenReturn(expectedCategories)
        `when`(genreGateway.update(any()))
            .thenAnswer(returnsFirstArg<Genre?>())

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id)
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories))
        Mockito.verify(genreGateway, times(1)).update(argThat { updatedGenre ->
            (expectedId == updatedGenre.id && expectedName == updatedGenre.name && expectedIsActive == updatedGenre.isActive
                    && expectedCategories == updatedGenre.categories && genre.createdAt == updatedGenre.createdAt
                    && genre.updatedAt.isBefore(updatedGenre.updatedAt)
                    && Objects.isNull(updatedGenre.deletedAt))
        })
    }

    @Test
    fun givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        val genre = Genre.newGenre("acao", true)
        val expectedId = genre.id
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf<CategoryID>()
        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        `when`(genreGateway.findById(any()))
            .thenReturn(Genre.with(genre))
        `when`(genreGateway.update(any()))
            .thenAnswer(returnsFirstArg<Genre?>())

        Assertions.assertTrue(genre.isActive)
        Assertions.assertNull(genre.deletedAt)

        val actualOutput = useCase.execute(aCommand)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id)
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(genreGateway, times(1)).update(argThat { updatedGenre ->
            (expectedId == updatedGenre.id && expectedName == updatedGenre.name && expectedIsActive == updatedGenre.isActive
                    && expectedCategories == updatedGenre.categories && genre.createdAt == updatedGenre.createdAt
                    && genre.updatedAt.isBefore(updatedGenre.updatedAt)
                    && Objects.nonNull(updatedGenre.deletedAt))
        })
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
        val genre = Genre.newGenre("acao", true)
        val expectedId = genre.id
        val expectedName = null
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be null"
        val aCommand = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        `when`(genreGateway.findById(any()))
            .thenReturn(Genre.with(genre))

        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { useCase.execute(aCommand) }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(0)).existsByIds(any())
        Mockito.verify(genreGateway, times(0)).update(any())
    }

    @Test
    fun givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
        val movies = CategoryID.from("123")
        val series = CategoryID.from("456")
        val documentaries = CategoryID.from("789")
        val genre = Genre.newGenre("acao", true)
        val expectedId = genre.id
        val expectedName = null
        val expectedIsActive = true
        val expectedCategories = listOf(movies, series, documentaries)
        val expectedErrorCount = 2
        val expectedErrorMessageOne = "Some categories could not be found: 456, 789"
        val expectedErrorMessageTwo = "'name' should not be null"
        val command = UpdateGenreCommand.with(
            expectedId.value,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        )

        `when`(genreGateway.findById(any()))
            .thenReturn(Genre.with(genre))
        `when`(categoryGateway.existsByIds(any()))
            .thenReturn(listOf(movies))

        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { useCase.execute(command) }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors[0].message)
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors[1].message)
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories))
        Mockito.verify(genreGateway, times(0)).update(any())
    }

    private fun asString(ids: List<CategoryID>) = ids.stream()
        .map(CategoryID::value)
        .toList()
}