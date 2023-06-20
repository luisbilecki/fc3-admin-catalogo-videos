package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.validation.firstError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
class CreateCategoryUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultCreateCategoryUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        `when`(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg<Any>())

        val useCase = DefaultCreateCategoryUseCase(categoryGateway)

        val actualOutput = useCase.execute(command).get()

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

    @Test
    fun givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be null"
        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)

        Mockito.verify(categoryGateway, times(0))!!.create(any())
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        `when`(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg<Any>())

        val actualOutput = useCase.execute(command).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        Mockito.verify(categoryGateway, times(1))!!.create(argThat { category ->
            (expectedName == category.name && expectedDescription == category.description && expectedIsActive == category.isActive
                    && Objects.nonNull(category.id)
                    && Objects.nonNull(category.createdAt)
                    && Objects.nonNull(category.updatedAt)
                    && Objects.nonNull(category.deletedAt))
        })
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "Gateway error"
        val expectedErrorCount = 1
        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        `when`(categoryGateway.create(any()))
                .thenThrow(IllegalStateException(expectedErrorMessage))
        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)
        Mockito.verify(categoryGateway, times(1))!!.create(argThat { category ->
            (expectedName == category.name && expectedDescription == category.description && expectedIsActive == category.isActive
                    && Objects.nonNull(category.id)
                    && Objects.nonNull(category.createdAt)
                    && Objects.nonNull(category.updatedAt)
                    && Objects.isNull(category.deletedAt))
        })
    }
}