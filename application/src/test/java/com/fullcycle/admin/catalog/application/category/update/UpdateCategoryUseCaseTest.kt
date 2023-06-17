package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
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
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import java.util.*


@ExtendWith(MockitoExtension::class)
class UpdateCategoryUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultUpdateCategoryUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

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
                .thenReturn(Category.with(category))
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

    @Test
    fun givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        val category = Category.newCategory("Film", null, true)
        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedId = category.id
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1
        val command = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)

        `when`(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Category.with(category))

        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)

        Mockito.verify(categoryGateway, times(0)).update(any())
    }

    @Test
    fun givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val category = Category.newCategory("Film", null, true)
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedId = category.id
        val aCommand = UpdateCategoryCommand.with(
                expectedId.value,
                expectedName,
                expectedDescription,
                expectedIsActive
        )
        `when`(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Category.with(category))
        `when`(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg<Category?>())

        Assertions.assertTrue(category.isActive)
        Assertions.assertNull(category.deletedAt)

        val actualOutput = useCase.execute(aCommand).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId))
        Mockito.verify(categoryGateway, times(1)).update(argThat { updatedCategory ->
            (expectedName == updatedCategory.name && expectedDescription == updatedCategory.description && expectedIsActive == updatedCategory.isActive && expectedId == updatedCategory.id && category.createdAt == updatedCategory.createdAt
                    && category.updatedAt.isBefore(updatedCategory.updatedAt)
                    && Objects.nonNull(updatedCategory.deletedAt))
        })
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        val category = Category.newCategory("Film", null, true)
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedId = category.id
        val expectedErrorCount = 1
        val expectedErrorMessage = "Gateway error"
        val aCommand = UpdateCategoryCommand.with(
                expectedId.value,
                expectedName,
                expectedDescription,
                expectedIsActive
        )

        `when`(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Category.with(category))
        `when`(categoryGateway.update(any()))
                .thenThrow(IllegalStateException(expectedErrorMessage))

        val notification = useCase.execute(aCommand).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)

        Mockito.verify(categoryGateway, times(1)).update(argThat { updatedCategory ->
            (expectedName == updatedCategory.name && expectedDescription == updatedCategory.description && expectedIsActive == updatedCategory.isActive && expectedId == updatedCategory.id && category.createdAt == updatedCategory.createdAt
                    && category.updatedAt.isBefore(updatedCategory.updatedAt)
                    && Objects.isNull(updatedCategory.deletedAt))
        })
    }

    @Test
    fun givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedId = "123"
        val expectedErrorCount = 1
        val expectedErrorMessage = "Category with ID 123 was not found"
        val command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        )

        `when`(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                .thenReturn(null)

        val actualException = Assertions.assertThrows(DomainException::class.java) { useCase.execute(command) }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)

        Mockito.verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)))
        Mockito.verify(categoryGateway, times(0)).update(any())
    }
}