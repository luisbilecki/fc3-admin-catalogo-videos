package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalog.domain.validation.firstError
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import java.util.*


@IntegrationTest
class UpdateCategoryUseCaseIT @Autowired constructor(
    private val useCase: UpdateCategoryUseCase,
    private val categoryRepository: CategoryRepository,
    @SpyBean private val categoryGateway: CategoryGateway
) {

    @Test
    fun givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        val category = Category.newCategory("Film", null, true)

        save(category)

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

        Assertions.assertEquals(1, categoryRepository!!.count())

        val actualOutput = useCase.execute(command).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        val actualCategory = categoryRepository.findById(expectedId.value).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertTrue(category.updatedAt.isBefore(actualCategory.updatedAt))
        Assertions.assertNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        val category = Category.newCategory("Film", null, true)

        save(category)

        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedId = category.id
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1
        val command = UpdateCategoryCommand.with(expectedId.value, expectedName, expectedDescription, expectedIsActive)
        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)
        Mockito.verify(categoryGateway, times(0))!!.update(any())
    }

    @Test
    fun givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        val category = Category.newCategory("Film", null, true)

        save(category)

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

        Assertions.assertTrue(category.isActive)
        Assertions.assertNull(category.deletedAt)

        val actualOutput = useCase.execute(aCommand).get()

        Assertions.assertNotNull(actualOutput)
        Assertions.assertNotNull(actualOutput.id)

        val actualCategory = categoryRepository.findById(expectedId.value).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertTrue(category.updatedAt.isBefore(actualCategory.updatedAt))
        Assertions.assertNotNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        val category = Category.newCategory("Film", null, true)

        save(category)

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedId = category.id
        val expectedErrorCount = 1
        val expectedErrorMessage = "Gateway error"
        val command = UpdateCategoryCommand.with(
            expectedId.value,
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        `when`(categoryGateway.update(any()))
            .thenThrow(IllegalStateException(expectedErrorMessage))

        val notification = useCase.execute(command).left

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size)
        Assertions.assertEquals(expectedErrorMessage, notification.firstError()!!.message)

        val actualCategory = categoryRepository.findById(expectedId.value).get()

        Assertions.assertEquals(category.name, actualCategory.name)
        Assertions.assertEquals(category.description, actualCategory.description)
        Assertions.assertEquals(category.isActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(category.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualCategory.deletedAt)
    }

    @Test
    fun givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedId = "123"
        val expectedErrorMessage = "Category with ID 123 was not found"
        val command = UpdateCategoryCommand.with(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive
        )

        val actualException = Assertions.assertThrows(NotFoundException::class.java) { useCase.execute(command) }

        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
    }

    private fun save(vararg aCategory: Category) {
        categoryRepository.saveAllAndFlush(
            Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList()
        )
    }
}