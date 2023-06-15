package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.application.UseCaseTest
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*


class CreateCategoryUseCaseTest : UseCaseTest() {
    @InjectMocks
    private val useCase: DefaultCreateCategoryUseCase

    @Mock
    private val categoryGateway: CategoryGateway? = null
    override val mocks: List<Any?>
        protected get() = java.util.List.of<Any?>(categoryGateway)

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        `when`(categoryGateway!!.create(any()))
                .thenAnswer(returnsFirstArg<Any>())
        val actualOutput = useCase.execute(aCommand).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id())
        Mockito.verify(categoryGateway, times(1))!!.create(argThat { aCategory ->
            (expectedName == aCategory.getName() && expectedDescription == aCategory.getDescription() && expectedIsActive == aCategory.isActive()
                    && Objects.nonNull(aCategory.getId())
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && Objects.isNull(aCategory.getDeletedAt()))
        })
    }

    @Test
    fun givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1
        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        val notification = useCase.execute(aCommand).getLeft()

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size())
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message())
        Mockito.verify(categoryGateway, times(0))!!.create(any())
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        `when`(categoryGateway!!.create(any()))
                .thenAnswer(returnsFirstArg<Any>())
        val actualOutput = useCase.execute(aCommand).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id())
        Mockito.verify(categoryGateway, times(1))!!.create(argThat { aCategory ->
            (expectedName == aCategory.getName() && expectedDescription == aCategory.getDescription() && expectedIsActive == aCategory.isActive()
                    && Objects.nonNull(aCategory.getId())
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && Objects.nonNull(aCategory.getDeletedAt()))
        })
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "Gateway error"
        val aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        `when`(categoryGateway!!.create(any()))
                .thenThrow(IllegalStateException(expectedErrorMessage))
        val notification: Unit = useCase.execute(aCommand).getLeft()

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size())
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message())
        Mockito.verify(categoryGateway, times(1))!!.create(argThat { aCategory ->
            (expectedName == aCategory.getName() && expectedDescription == aCategory.getDescription() && expectedIsActive == aCategory.isActive()
                    && Objects.nonNull(aCategory.getId())
                    && Objects.nonNull(aCategory.getCreatedAt())
                    && Objects.nonNull(aCategory.getUpdatedAt())
                    && Objects.isNull(aCategory.getDeletedAt()))
        })
    }
}