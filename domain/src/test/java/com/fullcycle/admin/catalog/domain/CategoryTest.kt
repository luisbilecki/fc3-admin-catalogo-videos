package com.fullcycle.admin.catalog.domain

import com.fullcycle.admin.catalog.domain.category.Category
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CategoryTest {
    @Test
    fun givenAValidParams_whenCallNewCategory_thenInstantiateNewCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        Assertions.assertNotNull(actualCategory)
        Assertions.assertNotNull(actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNull(actualCategory.deletedAt)
    }
}
