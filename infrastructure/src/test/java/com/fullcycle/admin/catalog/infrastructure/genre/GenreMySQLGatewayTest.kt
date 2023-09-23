package com.fullcycle.admin.catalog.infrastructure.genre

import com.fullcycle.admin.catalog.MySQLGatewayTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.infrastructure.category.CategoryMySQLGateway
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


@MySQLGatewayTest
open class GenreMySQLGatewayTest @Autowired constructor(
    private val categoryGateway: CategoryMySQLGateway,
    private val genreGateway: GenreMySQLGateway,
    private val genreRepository: GenreRepository
) {

    @Test
    fun testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway)
        Assertions.assertNotNull(genreGateway)
        Assertions.assertNotNull(genreRepository)
    }

    @Test
    fun givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
        val movies = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(movies!!.id)
        val genre = Genre.newGenre(expectedName, expectedIsActive)
        genre.addCategories(expectedCategories)

        val expectedId = genre.id
        Assertions.assertEquals(0, genreRepository.count())

        val actualGenre = genreGateway.create(genre)
        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertEquals(expectedId, actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertEquals(genre.updatedAt, actualGenre.updatedAt)
        Assertions.assertEquals(genre.deletedAt, actualGenre.deletedAt)

        Assertions.assertNull(actualGenre.deletedAt)
        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs())
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertEquals(genre.updatedAt, persistedGenre.updatedAt)
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val genre = Genre.newGenre(expectedName, expectedIsActive)
        val expectedId = genre.id
        Assertions.assertEquals(0, genreRepository.count())

        val actualGenre = genreGateway.create(genre)
        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertEquals(expectedId, actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre.categories)
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertEquals(genre.updatedAt, actualGenre.updatedAt)
        Assertions.assertEquals(genre.deletedAt, actualGenre.deletedAt)

        Assertions.assertNull(actualGenre.deletedAt)
        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs())
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertEquals(genre.updatedAt, persistedGenre.updatedAt)
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }


    @Test
    open fun givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
        val movies = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val series = categoryGateway.create(Category.newCategory("Séries", null, true))
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf(movies!!.id, series!!.id)
        val genre = Genre.newGenre("ac", expectedIsActive)
        val expectedId = genre.id
        Assertions.assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre))

        Assertions.assertEquals("ac", genre.name)
        Assertions.assertEquals(0, genre.categories.size)

        val actualGenre = genreGateway.update(
            Genre.with(genre)
                .update(expectedName, expectedIsActive, expectedCategories)!!
        )
        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertEquals(expectedId, actualGenre!!.id)
        Assertions.assertEquals(expectedName, actualGenre!!.name)
        Assertions.assertEquals(expectedIsActive, actualGenre!!.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre!!.categories)
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertEquals(genre.deletedAt, actualGenre!!.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs())
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    open fun givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
        val movies = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val series = categoryGateway.create(Category.newCategory("Séries", null, true))
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val genre = Genre.newGenre("ac", expectedIsActive)
        genre.addCategories(listOf(movies!!.id, series!!.id))
        val expectedId = genre.id
        Assertions.assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre))
        Assertions.assertEquals("ac", genre.name)
        Assertions.assertEquals(2, genre.categories.size)
        val actualGenre = genreGateway.update(
            Genre.with(genre)
                .update(expectedName, expectedIsActive, expectedCategories)!!
        )
        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertEquals(expectedId, actualGenre!!.id)
        Assertions.assertEquals(expectedName, actualGenre!!.name)
        Assertions.assertEquals(expectedIsActive, actualGenre!!.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre!!.categories)
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertEquals(genre.deletedAt, actualGenre!!.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs())
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    open fun givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = listOf<CategoryID>()
        val genre = Genre.newGenre(expectedName, false)
        val expectedId = genre.id
        Assertions.assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre))
        Assertions.assertFalse(genre.isActive)
        Assertions.assertNotNull(genre.deletedAt)

        val actualGenre = genreGateway.update(
            Genre.with(genre)
                .update(expectedName, expectedIsActive, expectedCategories)!!
        )
        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertEquals(expectedId, actualGenre!!.id)
        Assertions.assertEquals(expectedName, actualGenre!!.name)
        Assertions.assertEquals(expectedIsActive, actualGenre!!.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre!!.categories)
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertNull(genre.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs())
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    open fun givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
        val expectedName = "Ação"
        val expectedIsActive = false
        val expectedCategories = listOf<CategoryID>()
        val genre = Genre.newGenre(expectedName, true)
        val expectedId = genre.id
        Assertions.assertEquals(0, genreRepository.count())

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre))

        Assertions.assertTrue(genre.isActive)
        Assertions.assertNull(genre.deletedAt)

        val actualGenre = genreGateway.update(
            Genre.with(genre)
                .update(expectedName, expectedIsActive, expectedCategories)!!
        )
        Assertions.assertEquals(1, genreRepository.count())
        Assertions.assertEquals(expectedId, actualGenre!!.id)
        Assertions.assertEquals(expectedName, actualGenre!!.name)
        Assertions.assertEquals(expectedIsActive, actualGenre!!.isActive)
        Assertions.assertEquals(expectedCategories, actualGenre!!.categories)
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertNotNull(genre.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs())
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNotNull(persistedGenre.deletedAt)
    }
}