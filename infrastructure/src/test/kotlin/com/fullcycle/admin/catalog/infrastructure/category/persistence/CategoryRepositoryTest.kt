package com.fullcycle.admin.catalog.infrastructure.category.persistence

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.MySQLGatewayTest
import org.hibernate.PropertyValueException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException

@MySQLGatewayTest
open class CategoryRepositoryTest @Autowired constructor(
    private val categoryRepository: CategoryRepository
) {

    @Test
    fun givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        val expectedPropertyName = "name"
        val expectedMessage =
            "not-null property references a null or transient value : com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.name"
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val entity = CategoryJpaEntity.from(category)
        entity.name = null

        val actualException = Assertions.assertThrows(DataIntegrityViolationException::class.java) {
            categoryRepository.save(
                entity
            )
        }

        val actualCause = Assertions.assertInstanceOf(PropertyValueException::class.java, actualException.cause)

        Assertions.assertEquals(expectedPropertyName, actualCause.propertyName)
        Assertions.assertEquals(expectedMessage, actualCause.message)
    }

    @Test
    fun givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        val expectedPropertyName = "createdAt"
        val expectedMessage =
            "not-null property references a null or transient value : com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt"
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val entity = CategoryJpaEntity.from(category)
        entity.createdAt = null

        val actualException = Assertions.assertThrows(DataIntegrityViolationException::class.java) {
            categoryRepository.save(
                entity
            )
        }

        val actualCause =
            Assertions.assertInstanceOf(PropertyValueException::class.java, actualException.cause)

        Assertions.assertEquals(expectedPropertyName, actualCause.propertyName)
        Assertions.assertEquals(expectedMessage, actualCause.message)
    }

    @Test
    fun givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        val expectedPropertyName = "updatedAt"
        val expectedMessage =
            "not-null property references a null or transient value : com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.updatedAt"
        val category = Category.newCategory("Filmes", "A categoria mais assistida", true)
        val entity = CategoryJpaEntity.from(category)
        entity.updatedAt = null

        val actualException = Assertions.assertThrows(DataIntegrityViolationException::class.java) {
            categoryRepository.save(
                entity
            )
        }

        val actualCause = Assertions.assertInstanceOf(PropertyValueException::class.java, actualException.cause)

        Assertions.assertEquals(expectedPropertyName, actualCause.propertyName)
        Assertions.assertEquals(expectedMessage, actualCause.message!!)
    }
}