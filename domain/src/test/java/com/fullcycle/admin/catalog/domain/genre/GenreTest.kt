package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.List


class GenreTest {

    @Test
    fun givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = 0
        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        Assertions.assertNotNull(actualGenre)
        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories.size)
        Assertions.assertNotNull(actualGenre.createdAt)
        Assertions.assertNotNull(actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        val expectedName: String? = null
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be null"
        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { Genre.newGenre(expectedName, expectedIsActive) }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
        val expectedName = " "
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"
        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { Genre.newGenre(expectedName, expectedIsActive) }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
        val expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                
                """.trimIndent()
        val expectedIsActive = true
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' must be between 1 and 255 characters"
        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) { Genre.newGenre(expectedName, expectedIsActive) }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = 0
        val actualGenre = Genre.newGenre(expectedName, true)

        Assertions.assertNotNull(actualGenre)
        Assertions.assertTrue(actualGenre.isActive)
        Assertions.assertNull(actualGenre.deletedAt)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        actualGenre.deactivate()

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories.size)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNotNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = 0
        val actualGenre = Genre.newGenre(expectedName, false)

        Assertions.assertNotNull(actualGenre)
        Assertions.assertFalse(actualGenre.isActive)
        Assertions.assertNotNull(actualGenre.deletedAt)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        actualGenre.activate()

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories.size)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNotNull(actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(CategoryID.from("123"))
        val actualGenre = Genre.newGenre(expectedName, false)

        Assertions.assertNotNull(actualGenre)
        Assertions.assertFalse(actualGenre.isActive)
        Assertions.assertNotNull(actualGenre.deletedAt)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        actualGenre.update(expectedName, expectedIsActive, expectedCategories)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf(CategoryID.from("123"))
        val actualGenre = Genre.newGenre(expectedName, true)

        Assertions.assertNotNull(actualGenre)
        Assertions.assertTrue(actualGenre.isActive)
        Assertions.assertNull(actualGenre.deletedAt)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        actualGenre.update(expectedName, expectedIsActive, expectedCategories)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNotNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
        val expectedName = " "
        val expectedIsActive = true
        val expectedCategories = listOf(CategoryID.from("123"))
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be empty"
        val actualGenre = Genre.newGenre("acao", false)
        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) {
            actualGenre.update(
                expectedName,
                expectedIsActive,
                expectedCategories
            )
        }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }

    @Test
    fun givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
        val expectedName: String? = null
        val expectedIsActive = true
        val expectedCategories = listOf(CategoryID.from("123"))
        val expectedErrorCount = 1
        val expectedErrorMessage = "'name' should not be null"
        val actualGenre = Genre.newGenre("acao", false)
        val actualException = Assertions.assertThrows(
            NotificationException::class.java
        ) {
            actualGenre.update(
                expectedName,
                expectedIsActive,
                expectedCategories
            )
        }

        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
        Assertions.assertEquals(expectedErrorMessage, actualException.errors[0].message)
    }


    @Test
    fun givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = ArrayList<CategoryID>()
        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)
        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt
        
        Assertions.assertDoesNotThrow {
            actualGenre.update(
                expectedName,
                expectedIsActive,
                null
            )
        }
        
        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(seriesID, moviesID)
        
        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)
        
        Assertions.assertEquals(0, actualGenre.categories.size)
        
        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt
        
        actualGenre.addCategory(seriesID)
        actualGenre.addCategory(moviesID)
        
        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = ArrayList<CategoryID>()
        
        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)
        
        Assertions.assertEquals(0, actualGenre.categories.size)
        
        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt     
        actualGenre.addCategory(null)
        
        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertEquals(actualUpdatedAt, actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(moviesID)
        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID))

        Assertions.assertEquals(2, actualGenre.categories.size)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        actualGenre.removeCategory(seriesID)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt))
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK() {
        val seriesID = CategoryID.from("123")
        val moviesID = CategoryID.from("456")
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(seriesID, moviesID)
        val actualGenre = Genre.newGenre(expectedName, expectedIsActive)

        actualGenre.update(expectedName, expectedIsActive, expectedCategories)

        Assertions.assertEquals(2, actualGenre.categories.size)

        val actualCreatedAt = actualGenre.createdAt
        val actualUpdatedAt = actualGenre.updatedAt

        actualGenre.removeCategory(null)

        Assertions.assertNotNull(actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt)
        Assertions.assertEquals(actualUpdatedAt, actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }
}