package com.fullcycle.admin.catalog.domain

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.validation.handler.ThrowsValidationHandler
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

    @Test
    fun givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = null;
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be null"
        val expectedErrorCount = 1
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        val actualException = Assertions.assertThrows(DomainException::class.java) { actualCategory.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
    }

    @Test
    fun givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "";
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' should not be empty"
        val expectedErrorCount = 1
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        val actualException = Assertions.assertThrows(DomainException::class.java) { actualCategory.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
    }

    @Test
    fun givenAnInvalidEmptyNameLengthThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "Fi ";
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val expectedErrorCount = 1
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        val actualException = Assertions.assertThrows(DomainException::class.java) { actualCategory.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
    }

    @Test
    fun givenAnInvalidEmptyNameLengthGreaterOrMoreThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "Gostaria de enfatizar que a execução dos pontos do programa estende o alcance e a importância " +
                "do investimento em reciclagem técnica. É importante questionar o quanto a complexidade dos estudos efetuados " +
                "prepara-nos para enfrentar situações atípicas decorrentes das diretrizes de desenvolvimento para o futuro. " +
                "Assim mesmo, a contínua expansão de nossa atividade ainda não demonstrou convincentemente que vai participar " +
                "na mudança dos índices pretendidos. No entanto, não podemos esquecer que a competitividade nas transações comerciais causa impacto indireto " +
                "na reavaliação das posturas dos órgãos dirigentes com relação às suas atribuições. Do mesmo modo, " +
                "o novo modelo estrutural aqui preconizado desafia a capacidade de equalização das condições financeiras" +
                " e administrativas exigidas.";
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedErrorMessage = "'name' must be between 3 and 255 characters"
        val expectedErrorCount = 1
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        val actualException = Assertions.assertThrows(DomainException::class.java) { actualCategory.validate(ThrowsValidationHandler()) }

        Assertions.assertEquals(expectedErrorMessage, actualException.errors.first().message)
        Assertions.assertEquals(expectedErrorCount, actualException.errors.size)
    }


    @Test
    fun givenAnInvalidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        val expectedName = "New name";
        val expectedDescription = ""
        val expectedIsActive = true
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)

        Assertions.assertDoesNotThrow { actualCategory.validate(ThrowsValidationHandler()) }
    }
    @Test
    fun givenAValidFalseIsActive_whenCallNewCategory_thenInstantiateNewCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        Assertions.assertNotNull(actualCategory)
        Assertions.assertNotNull(actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNotNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false

        val category = Category.newCategory(expectedName, expectedDescription, true)
        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        val createdAt = category.createdAt
        val updatedAt = category.updatedAt
        Assertions.assertTrue(category.isActive)
        Assertions.assertNull(category.deletedAt)

        val actualCategory = category.deactivate()
        Assertions.assertDoesNotThrow { actualCategory.validate(ThrowsValidationHandler()) }
        Assertions.assertEquals(category.id, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(createdAt, actualCategory.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNotNull(actualCategory.deletedAt)
    }

    @Test
    fun givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val category = Category.newCategory(expectedName, expectedDescription, false)
        Assertions.assertDoesNotThrow { category.validate(ThrowsValidationHandler()) }

        val createdAt = category.createdAt
        val updatedAt = category.updatedAt
        Assertions.assertFalse(category.isActive)
        Assertions.assertNotNull(category.deletedAt)

        val actualCategory = category.activate()
        Assertions.assertDoesNotThrow { actualCategory.validate(ThrowsValidationHandler()) }
        Assertions.assertEquals(category.id, actualCategory.id)
        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertEquals(createdAt, actualCategory.createdAt)
        Assertions.assertTrue(actualCategory.updatedAt.isAfter(updatedAt))
        Assertions.assertNull(actualCategory.deletedAt)
    }
}
