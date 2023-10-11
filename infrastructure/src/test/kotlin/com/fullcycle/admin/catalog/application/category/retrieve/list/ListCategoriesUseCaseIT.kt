package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import java.util.stream.Stream


@IntegrationTest
class ListCategoriesUseCaseIT @Autowired constructor(
    private val useCase: ListCategoriesUseCase,
    private val categoryRepository: CategoryRepository
) {

    @BeforeEach
    fun mockUp() {
        val categories = Stream.of(
            Category.newCategory("Filmes", null, true),
            Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
            Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
            Category.newCategory("Documentários", null, true),
            Category.newCategory("Sports", null, true),
            Category.newCategory("Kids", "Categoria para crianças", true),
            Category.newCategory("Series", null, true)
        )
            .map(CategoryJpaEntity::from)
            .toList()
        categoryRepository.saveAllAndFlush(categories)
    }

    @Test
    fun givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 10
        val expectedTerms = "ji1j3i 1j3i1oj"
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedItemsCount = 0
        val expectedTotal = 0L
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)
        val actualResult = useCase.execute(query)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
    }

    @ParameterizedTest
    @CsvSource(
        "fil,0,10,1,1,Filmes",
        "net,0,10,1,1,Netflix Originals",
        "ZON,0,10,1,1,Amazon Originals",
        "KI,0,10,1,1,Kids",
        "crianças,0,10,1,1,Kids",
        "da Amazon,0,10,1,1,Amazon Originals"
    )
    fun givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
        expectedTerms: String?,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoryName: String?
    ) {
        val expectedSort = "name"
        val expectedDirection = "asc"
        val query = SearchQuery(
            expectedPage, expectedPerPage,
            expectedTerms!!, expectedSort, expectedDirection
        )
        val actualResult = useCase.execute(query)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedCategoryName, actualResult.items.first().name)
    }

    @ParameterizedTest
    @CsvSource(
        "name,asc,0,10,7,7,Amazon Originals",
        "name,desc,0,10,7,7,Sports",
        "createdAt,asc,0,10,7,7,Filmes",
        "createdAt,desc,0,10,7,7,Series"
    )
    fun givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
        expectedSort: String?,
        expectedDirection: String?,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoryName: String?
    ) {
        val expectedTerms = ""
        val query = SearchQuery(
            expectedPage, expectedPerPage, expectedTerms,
            expectedSort!!, expectedDirection!!
        )
        val actualResult = useCase.execute(query)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedCategoryName, actualResult.items.first().name)
    }

    @ParameterizedTest
    @CsvSource(
        "0,2,2,7,Amazon Originals;Documentários",
        "1,2,2,7,Filmes;Kids",
        "2,2,2,7,Netflix Originals;Series",
        "3,2,1,7,Sports"
    )
    fun givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedCategoriesName: String
    ) {
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTerms = ""
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)
        val actualResult = useCase.execute(query)

        Assertions.assertEquals(expectedItemsCount, actualResult.items.size)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)

        for ((index, expectedName) in expectedCategoriesName.split(";".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray().withIndex()) {
            val actualName = actualResult.items[index].name
            Assertions.assertEquals(expectedName, actualName)
        }
    }
}