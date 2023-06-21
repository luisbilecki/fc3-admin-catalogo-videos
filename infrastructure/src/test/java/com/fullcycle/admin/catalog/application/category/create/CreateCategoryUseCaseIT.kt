package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.validation.firstError
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean

@IntegrationTest
class CreateCategoryUseCaseIT @Autowired constructor(
    private val useCase: CreateCategoryUseCase,
    private val categoryRepository: CategoryRepository,
    @SpyBean private val categoryGateway: CategoryGateway
) {

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        Assertions.assertEquals(0, categoryRepository.count())

        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        val actualOutput = useCase.execute(command).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        Assertions.assertEquals(1, categoryRepository.count())

        val actualCategory = categoryRepository.findById(actualOutput.id).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1

        Assertions.assertEquals(0, categoryRepository.count())

        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)
        Assertions.assertEquals(0, categoryRepository.count())

        verify(categoryGateway, times(0)).create(any())
    }

    @Test
    fun givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        Assertions.assertEquals(0, categoryRepository.count())

        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)
        val actualOutput = useCase.execute(command).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)
        Assertions.assertEquals(1, categoryRepository.count())

        val actualCategory = categoryRepository.findById(actualOutput.id).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNotNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "Gateway error"
        val command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive)

        `when`(categoryGateway.create(any<Category>()))
            .thenThrow(IllegalStateException(expectedErrorMessage))
        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)
    }
}