package com.fullcycle.admin.catalog.application.category.delete

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.times


@ExtendWith(MockitoExtension::class)
class DeleteCategoryUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultDeleteCategoryUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway
    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway, useCase)
    }

    @Test
    fun givenAValidCategoryId_whenCallsDeleteCategory_shouldBeOK() {
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val expectedId = category.id

        Assertions.assertDoesNotThrow { useCase.execute(expectedId.value) }

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId))
    }

    @Test
    fun givenAInvalidCategoryId_whenCallsDeleteCategory_shouldThrowsNotFound() {
        val expectedId = CategoryID.from("123")

        Assertions.assertThrows(DomainException::class.java) { useCase.execute(expectedId.value) }

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId))
    }
}