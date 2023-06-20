package com.fullcycle.admin.catalog.infrastructure.category

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.category.CategorySearchQuery
import com.fullcycle.admin.catalog.infrastructure.MySQLGatewayTest
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


@MySQLGatewayTest
open class CategoryMySQLGatewayTest {

    @Autowired
    private lateinit var categoryGateway: CategoryMySQLGateway

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Test
    fun givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        Assertions.assertEquals(0, categoryRepository.count())

        val actualCategory = categoryGateway.create(category)

        Assertions.assertEquals(1, categoryRepository.count())
        Assertions.assertEquals(category.id, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(category.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualCategory.deletedAt)
        Assertions.assertNull(actualCategory.deletedAt)

        val actualEntity = categoryRepository.findById(category.id.value).get()

        Assertions.assertEquals(category.id.value, actualEntity.id)
        Assertions.assertEquals(expectedName, actualEntity.name)
        Assertions.assertEquals(expectedDescription, actualEntity.description)
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive)
        Assertions.assertEquals(category.createdAt, actualEntity.createdAt)
        Assertions.assertEquals(category.updatedAt, actualEntity.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualEntity.deletedAt)
        Assertions.assertNull(actualEntity.deletedAt)
    }

    @Test
    open fun givenAnInvalidCategoryName_whenCallsCreate_shouldReturnANewCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertEquals(0, categoryRepository.count())

        val actualCategory = categoryGateway.create(category)

        Assertions.assertEquals(1, categoryRepository.count())
        Assertions.assertEquals(category.id, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(category.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualCategory.deletedAt)
        Assertions.assertNull(actualCategory.deletedAt)

        val actualEntity = categoryRepository.findById(category.id.value).get()

        Assertions.assertEquals(category.id.value, actualEntity.id)
        Assertions.assertEquals(expectedName, actualEntity.name)
        Assertions.assertEquals(expectedDescription, actualEntity.description)
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive)
        Assertions.assertEquals(category.createdAt, actualEntity.createdAt)
        Assertions.assertEquals(category.updatedAt, actualEntity.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualEntity.deletedAt)
        Assertions.assertNull(actualEntity.deletedAt)
    }

    @Test
    open fun givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val category = Category.newCategory("Film", null, expectedIsActive)

        Assertions.assertEquals(0, categoryRepository.count())
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category))
        Assertions.assertEquals(1, categoryRepository.count())

        val actualInvalidEntity = categoryRepository.findById(category.id.value).get()

        Assertions.assertEquals("Film", actualInvalidEntity.name)
        Assertions.assertNull(actualInvalidEntity.description)
        Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive)

        val aUpdatedCategory = Category.with(category).update(expectedName, expectedDescription, expectedIsActive)

        val actualCategory = categoryGateway.update(aUpdatedCategory)

        Assertions.assertEquals(1, categoryRepository.count())
        Assertions.assertEquals(category.id, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertTrue(category.updatedAt.isBefore(actualCategory.updatedAt))
        Assertions.assertEquals(category.deletedAt, actualCategory.deletedAt)
        Assertions.assertNull(actualCategory.deletedAt)

        val actualEntity = categoryRepository.findById(category.id.value).get()

        Assertions.assertEquals(category.id.value, actualEntity.id)
        Assertions.assertEquals(expectedName, actualEntity.name)
        Assertions.assertEquals(expectedDescription, actualEntity.description)
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive)
        Assertions.assertEquals(category.createdAt, actualEntity.createdAt)
        Assertions.assertTrue(category.updatedAt.isBefore(actualCategory.updatedAt))
        Assertions.assertEquals(category.deletedAt, actualEntity.deletedAt)
        Assertions.assertNull(actualEntity.deletedAt)
    }

    @Test
    fun givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        val category = Category.newCategory("Filmes", null, true)

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category))

        Assertions.assertEquals(1, categoryRepository.count())

        categoryGateway.deleteById(category.id)

        Assertions.assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count())

        categoryGateway.deleteById(CategoryID.from("invalid"))

        Assertions.assertEquals(0, categoryRepository.count())
    }

    @Test
    fun givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category))

        Assertions.assertEquals(1, categoryRepository.count())

        val actualCategory = categoryGateway.findById(category.id)!!

        Assertions.assertEquals(1, categoryRepository.count())
        Assertions.assertEquals(category.id, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory.createdAt)
        Assertions.assertEquals(category.updatedAt, actualCategory.updatedAt)
        Assertions.assertEquals(category.deletedAt, actualCategory.deletedAt)
        Assertions.assertNull(actualCategory.deletedAt)
    }

    @Test
    fun givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count())

        val actualCategory = categoryGateway.findById(CategoryID.from("empty"))

        Assertions.assertNull(actualCategory)
    }

    @Test
    fun givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 3L
        val movies = Category.newCategory("Filmes", null, true)
        val series = Category.newCategory("Séries", null, true)
        val documentaries = Category.newCategory("Documentários", null, true)

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.saveAll(
            listOf(
                CategoryJpaEntity.from(movies), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)
            )
        )

        Assertions.assertEquals(3, categoryRepository.count())

        val query = CategorySearchQuery(0, 1, "", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedPerPage, actualResult.items.size)
        Assertions.assertEquals(documentaries.id, actualResult.items.first().id)
    }

    @Test
    fun givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 0L

        Assertions.assertEquals(0, categoryRepository.count())

        val query = CategorySearchQuery(0, 1, "", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(0, actualResult.items.size)
    }

    @Test
    fun givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 3L
        val movies = Category.newCategory("Filmes", null, true)
        val series = Category.newCategory("Séries", null, true)
        val documentaries = Category.newCategory("Documentários", null, true)

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.saveAll(
            listOf(
                CategoryJpaEntity.from(movies), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)
            )
        )

        Assertions.assertEquals(3, categoryRepository.count())

        var query = CategorySearchQuery(0, 1, "", "name", "asc")
        var actualResult = categoryGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedPerPage, actualResult.items.size)
        Assertions.assertEquals(documentaries.id, actualResult.items.first().id)

        // Page 1
        expectedPage = 1
        query = CategorySearchQuery(1, 1, "", "name", "asc")
        actualResult = categoryGateway.findAll(query)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedPerPage, actualResult.items.size)
        Assertions.assertEquals(movies.id, actualResult.items.first().id)

        // Page 2
        expectedPage = 2
        query = CategorySearchQuery(2, 1, "", "name", "asc")
        actualResult = categoryGateway.findAll(query)
        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedPerPage, actualResult.items.size)
        Assertions.assertEquals(series.id, actualResult.items.first().id)
    }

    @Test
    fun givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 1L
        val movies = Category.newCategory("Filmes", null, true)
        val series = Category.newCategory("Séries", null, true)
        val documentaries = Category.newCategory("Documentários", null, true)

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.saveAll(
            listOf(
                CategoryJpaEntity.from(movies), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)
            )
        )

        Assertions.assertEquals(3, categoryRepository.count())

        val query = CategorySearchQuery(0, 1, "doc", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedPerPage, actualResult.items.size)
        Assertions.assertEquals(documentaries.id, actualResult.items.first().id)
    }

    @Test
    fun givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTotal = 1L
        val movies = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val series = Category.newCategory("Séries", "Uma categoria assistida", true)
        val documentaries = Category.newCategory("Documentários", "A categoria menos assistida", true)

        Assertions.assertEquals(0, categoryRepository.count())

        categoryRepository.saveAll(
            listOf(
                CategoryJpaEntity.from(movies), CategoryJpaEntity.from(series), CategoryJpaEntity.from(documentaries)
            )
        )

        Assertions.assertEquals(3, categoryRepository.count())

        val query = CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc")
        val actualResult = categoryGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualResult.currentPage)
        Assertions.assertEquals(expectedPerPage, actualResult.perPage)
        Assertions.assertEquals(expectedTotal, actualResult.total)
        Assertions.assertEquals(expectedPerPage, actualResult.items.size)
        Assertions.assertEquals(movies.id, actualResult.items.first().id)
    }
}