package com.fullcycle.admin.catalog.application.category.delete

import com.fullcycle.admin.catalog.application.UseCaseTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.eq
import org.mockito.kotlin.times


class DeleteCategoryUseCaseTest : UseCaseTest() {

    @InjectMocks
    private lateinit var useCase: DefaultDeleteCategoryUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    override val mocks: List<Any?>
        get() = listOf(categoryGateway)

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val expectedId = category.id

        doNothing()
                .`when`(categoryGateway).deleteById(eq(expectedId))

        Assertions.assertDoesNotThrow { useCase.execute(expectedId.value) }

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId))
    }

    @Test
    fun givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        val expectedId = CategoryID.from("123")

        doNothing()
                .`when`(categoryGateway).deleteById(eq(expectedId))

        Assertions.assertDoesNotThrow { useCase.execute(expectedId.value) }

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId))
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val expectedId = category.id

        doThrow(IllegalStateException("Gateway error"))
                .`when`(categoryGateway).deleteById(eq(expectedId))

        Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(expectedId.value) }

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId))
    }
}