package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


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
}