package com.fullcycle.admin.catalog.application.category.retrieve.get

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq

@ExtendWith(MockitoExtension::class)
class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultGetCategoryByIdUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        val expectedId = category.id

        `when`(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Category.with(category))

        val actualCategory = useCase.execute(expectedId.value)

        Assertions.assertEquals(expectedId, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(category.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualCategory.deletedAt)
    }

    @Test
    fun givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedErrorMessage = "Category with ID 123 was not found"
        val expectedId = CategoryID.from("123")

        `when`(categoryGateway.findById(eq(expectedId)))
            .thenReturn(null)

        val actualException = Assertions.assertThrows(
            NotFoundException::class.java
        ) { useCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val expectedErrorMessage = "Gateway error"
        val expectedId = CategoryID.from("123")

        `when`(categoryGateway.findById(eq(expectedId)))
            .thenThrow(IllegalStateException(expectedErrorMessage))

        val actualException = Assertions.assertThrows(
            IllegalStateException::class.java
        ) { useCase.execute(expectedId.value) }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }
}