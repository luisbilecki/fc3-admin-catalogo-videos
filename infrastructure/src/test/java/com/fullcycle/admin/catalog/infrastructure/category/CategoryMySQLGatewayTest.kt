package com.fullcycle.admin.catalog.infrastructure.category

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
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
        Assertions.assertEquals(category.id, actualCategory!!.id)
        Assertions.assertEquals(expectedName, actualCategory!!.name)
        Assertions.assertEquals(expectedDescription, actualCategory!!.description)
        Assertions.assertEquals(expectedIsActive, actualCategory!!.isActive)
        Assertions.assertEquals(category.createdAt, actualCategory!!.createdAt)
        Assertions.assertTrue(category.updatedAt.isBefore(actualCategory!!.updatedAt))
        Assertions.assertEquals(category.deletedAt, actualCategory!!.deletedAt)
        Assertions.assertNull(actualCategory!!.deletedAt)

        val actualEntity = categoryRepository.findById(category.id.value).get()

        Assertions.assertEquals(category.id.value, actualEntity.id)
        Assertions.assertEquals(expectedName, actualEntity.name)
        Assertions.assertEquals(expectedDescription, actualEntity.description)
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive)
        Assertions.assertEquals(category.createdAt, actualEntity.createdAt)
        Assertions.assertTrue(category.updatedAt.isBefore(actualCategory!!.updatedAt))
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
}