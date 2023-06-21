package com.fullcycle.admin.catalog.application.category.delete

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import java.util.*


@IntegrationTest
class DeleteCategoryUseCaseIT @Autowired constructor(
    private val useCase: DeleteCategoryUseCase,
    private val categoryRepository: CategoryRepository,
    @SpyBean private val categoryGateway: CategoryGateway
) {

    @Test
    fun givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val expectedId = category.id

        save(category)

        Assertions.assertEquals(1, categoryRepository!!.count())
        Assertions.assertDoesNotThrow { useCase!!.execute(expectedId.value) }
        Assertions.assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        val expectedId = CategoryID.from("123")

        Assertions.assertEquals(0, categoryRepository!!.count())
        Assertions.assertDoesNotThrow { useCase!!.execute(expectedId.value) }
        Assertions.assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val expectedId = category.id

        `when`(categoryGateway.deleteById(eq(expectedId)))
            .thenThrow(IllegalStateException("Gateway error"))

        Assertions.assertThrows(IllegalStateException::class.java) { useCase!!.execute(expectedId.value) }

        Mockito.verify(categoryGateway, times(1))!!.deleteById(eq(expectedId))
    }

    private fun save(vararg aCategory: Category) {
        categoryRepository.saveAllAndFlush(
            Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList()
        )
    }
}