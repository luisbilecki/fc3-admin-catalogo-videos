package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategorySearchQuery
import com.fullcycle.admin.catalog.domain.pagination.Pagination
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
import java.util.List

@ExtendWith(MockitoExtension::class)
class ListCategoriesUseCaseTest {

    @InjectMocks
    private lateinit var useCase: DefaultListCategoriesUseCase

    @Mock
    private lateinit var categoryGateway: CategoryGateway

    @BeforeEach
    fun cleanUp() {
        Mockito.reset(categoryGateway)
    }

    @Test
    fun givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        val categories = List.of(
            Category.newCategory("Filmes", null, true),
            Category.newCategory("Series", null, true)
        )
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val aQuery = CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)
        val expectedPagination = Pagination<Category>(expectedPage, expectedPerPage, categories.size.toLong(), categories)
        val expectedItemsCount = 2
        val expectedResult = expectedPagination.map(CategoryListOutput::from)
        `when`(categoryGateway!!.findAll(eq(aQuery)))
                .thenReturn(expectedPagination)
        val actualResult = useCase.execute(aQuery)
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size())
        Assertions.assertEquals(expectedResult, actualResultexpectedResult)
        Assertions.assertEquals(expectedPage, actualResult.page())
        Assertions.assertEquals(expectedPerPage, actualResult.perPage())
        Assertions.assertEquals(categories.size, actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
    }
}