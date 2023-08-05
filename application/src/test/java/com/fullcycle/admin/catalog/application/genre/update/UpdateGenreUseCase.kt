package com.fullcycle.admin.catalog.application.genre.update

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
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
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import java.util.Objects

@ExtendWith(MockitoExtension::class)
class UpdateGenreUseCase {

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
            expectedCategories
        )
        `when`(genreGateway.findById(any()))
            .thenReturn(Genre.with(genre))
        `when`(genreGateway.update(any()))
            .thenAnswer(returnsFirstArg<Genre?>())

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertEquals(expectedId.value, actualOutput.id())
        Mockito.verify(genreGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(genreGateway, times(1))
            .update(argThat { updatedGenre ->
                (expectedId == updatedGenre.id && expectedName == updatedGenre.name && expectedIsActive == updatedGenre.isActive
                        && expectedCategories == updatedGenre.categories && genre.createdAt == updatedGenre.createdAt
                        && genre.updatedAt.isBefore(updatedGenre.updatedAt)
                        && Objects.isNull(updatedGenre.deletedAt))
            })
    }
}