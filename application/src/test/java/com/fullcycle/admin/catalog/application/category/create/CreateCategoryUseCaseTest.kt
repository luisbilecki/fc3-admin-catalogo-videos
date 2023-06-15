package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import java.util.*


class CreateCategoryUseCaseTest {

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        val categoryGateway = Mockito.mock(CategoryGateway::class.java)
        `when`(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg<Any>())

        val useCase = DefaultCreateCategoryUseCase(categoryGateway)

        val actualOutput = useCase.execute(command)

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        Mockito.verify(categoryGateway, times(1)).create(argThat { category ->
            (expectedName == category.name && expectedDescription == category.description && expectedIsActive == category.isActive
                    && Objects.nonNull(category.id)
                    && Objects.nonNull(category.createdAt)
                    && Objects.nonNull(category.updatedAt)
                    && Objects.isNull(category.deletedAt))
        })
    }
}