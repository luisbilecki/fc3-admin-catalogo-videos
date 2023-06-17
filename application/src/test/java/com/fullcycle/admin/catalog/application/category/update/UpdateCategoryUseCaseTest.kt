package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
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
class UpdateCategoryUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultUpdateCategoryUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val category = Category.newCategory("Film", null, true)

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedId = category.id
        val command = UpdateCategoryCommand.with(
                expectedId.value,
                expectedName,
                expectedDescription,
                expectedIsActive
        )

        `when`(categoryGateway.findById(eq(expectedId)))
                .thenReturn(category)
        `when`(categoryGateway.update(any<Category>()))
                .thenAnswer(returnsFirstArg<Category?>())

        val actualOutput = useCase.execute(command).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(1)).update(argThat { updatedCategory ->
            (
                    expectedName == updatedCategory.name
                            && expectedDescription == updatedCategory.description
                            && expectedIsActive == updatedCategory.isActive
                            && expectedId == updatedCategory.id
                            && category.createdAt == updatedCategory.createdAt
                            && category.updatedAt.isBefore(updatedCategory.updatedAt)
                    && Objects.isNull(updatedCategory.deletedAt))
        })
    }
}