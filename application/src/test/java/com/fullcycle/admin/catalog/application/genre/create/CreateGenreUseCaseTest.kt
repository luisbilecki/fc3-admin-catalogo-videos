package com.fullcycle.admin.catalog.application.genre.create

import com.fullcycle.admin.catalog.domain.category.CategoryID
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
import org.mockito.kotlin.times
import java.util.*

@ExtendWith(MockitoExtension::class)
class CreateGenreUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultCreateGenreUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @Mock
    private lateinit var genreGateway: GenreGateway
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

    private fun asString(categories: List<CategoryID>): List<String> {
        return categories.stream()
            .map(CategoryID::value)
            .toList()
    }
}