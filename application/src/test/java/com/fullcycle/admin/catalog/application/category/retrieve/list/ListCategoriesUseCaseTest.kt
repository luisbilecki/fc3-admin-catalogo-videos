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
        val categories = listOf(
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

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(categories.size.toLong(), actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        val categories = listOf<Category>()
        val categoriesSize = 0L

        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"

        val aQuery = CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val expectedPagination = Pagination(expectedPage, expectedPerPage, categoriesSize, categories)

        val expectedItemsCount = 0
        val expectedResult = expectedPagination.map<Any>(CategoryListOutput::from)

        `when`(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination)

        val actualResult = useCase.execute(aQuery)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedResult, actualResult)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(categoriesSize, actualResult.total)
    }

    @Test
    fun givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = ""
        val expectedSort = "createdAt"
        val expectedDirection = "asc"
        val expectedErrorMessage = "Gateway error"

        val aQuery = CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        `when`(categoryGateway.findAll(eq(aQuery)))
                .thenThrow(IllegalStateException(expectedErrorMessage))

        val actualException = Assertions.assertThrows(IllegalStateException::class.java) { useCase.execute(aQuery) }

        Assertions.assertEquals(expectedErrorMessage, actualException.message)
    }
}