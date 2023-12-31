package com.fullcycle.admin.catalog.infrastructure.genre

import com.fullcycle.admin.catalog.MySQLGatewayTest
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreID
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.infrastructure.category.CategoryMySQLGateway
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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
    fun givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
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
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.categories))
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertEquals(genre.deletedAt, actualGenre!!.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()))
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
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
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.categories))
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertEquals(genre.deletedAt, actualGenre!!.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()))
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
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
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.categories))
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertNull(genre.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
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
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.categories))
        Assertions.assertEquals(genre.createdAt, actualGenre!!.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(actualGenre!!.updatedAt))
        Assertions.assertNotNull(genre.deletedAt)

        val persistedGenre = genreRepository.findById(expectedId.value).get()
        Assertions.assertEquals(expectedName, persistedGenre.name)
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive)
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()))
        Assertions.assertEquals(genre.createdAt, persistedGenre.createdAt)
        Assertions.assertTrue(genre.updatedAt.isBefore(persistedGenre.updatedAt))
        Assertions.assertEquals(genre.deletedAt, persistedGenre.deletedAt)
        Assertions.assertNotNull(persistedGenre.deletedAt)
    }

    @Test
    fun givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        val genre = Genre.newGenre("Ação", true)
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre))
        Assertions.assertEquals(1, genreRepository.count())

        genreGateway.deleteById(genre.id)
        Assertions.assertEquals(0, genreRepository.count())
    }

    @Test
    fun givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
        Assertions.assertEquals(0, genreRepository.count())
        genreGateway.deleteById(GenreID.from("123"))
        Assertions.assertEquals(0, genreRepository.count())
    }

    @Test
    fun givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
        val movies = categoryGateway.create(Category.newCategory("Filmes", null, true))
        val series = categoryGateway.create(Category.newCategory("Séries", null, true))
        val expectedName = "Ação"
        val expectedIsActive = true
        val expectedCategories = java.util.List.of(movies!!.id, series!!.id)
        val genre = Genre.newGenre(expectedName, expectedIsActive)
        genre.addCategories(expectedCategories)
        val expectedId = genre.id

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre))
        Assertions.assertEquals(1, genreRepository.count())

        val actualGenre = genreGateway.findById(expectedId)!!

        Assertions.assertEquals(expectedId, actualGenre.id)
        Assertions.assertEquals(expectedName, actualGenre.name)
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive)
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.categories))
        Assertions.assertEquals(genre.createdAt, actualGenre.createdAt)
        Assertions.assertEquals(genre.updatedAt, actualGenre.updatedAt)
        Assertions.assertNull(actualGenre.deletedAt)
    }

    @Test
    fun givenAInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
        val expectedId = GenreID.from("123")
        Assertions.assertEquals(0, genreRepository.count())

        val actualGenre = genreGateway.findById(expectedId)
        Assertions.assertTrue(actualGenre != null)
    }

    @Test
    fun givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        val expectedPage = 0
        val expectedPerPage = 1
        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"
        val expectedTotal = 0
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualPage = genreGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualPage.currentPage)
        Assertions.assertEquals(expectedPerPage, actualPage.perPage)
        Assertions.assertEquals(expectedTotal, actualPage.total)
        Assertions.assertEquals(expectedTotal, actualPage.items.size)
    }

    @ParameterizedTest
    @CsvSource(
        "aç,0,10,1,1,Ação",
        "dr,0,10,1,1,Drama",
        "com,0,10,1,1,Comédia romântica",
        "cien,0,10,1,1,Ficção científica",
        "terr,0,10,1,1,Terror"
    )
    fun givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
        expectedTerms: String?,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedGenreName: String?
    ) {
        mockGenres()
        val expectedSort = "name"
        val expectedDirection = "asc"
        val query = SearchQuery(
            expectedPage, expectedPerPage,
            expectedTerms!!, expectedSort, expectedDirection
        )

        val actualPage = genreGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualPage.currentPage)
        Assertions.assertEquals(expectedPerPage, actualPage.perPage)
        Assertions.assertEquals(expectedTotal, actualPage.total)
        Assertions.assertEquals(expectedItemsCount, actualPage.items.size)
        Assertions.assertEquals(expectedGenreName, actualPage.items[0].name)
    }

    @ParameterizedTest
    @CsvSource(
        "name,asc,0,10,5,5,Ação",
        "name,desc,0,10,5,5,Terror",
        "createdAt,asc,0,10,5,5,Comédia romântica",
        "createdAt,desc,0,10,5,5,Ficção científica"
    )
    fun givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
        expectedSort: String?,
        expectedDirection: String?,
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedGenreName: String?
    ) {
        mockGenres()
        val expectedTerms = ""
        val query = SearchQuery(
            expectedPage, expectedPerPage, expectedTerms,
            expectedSort!!, expectedDirection!!
        )

        val actualPage = genreGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualPage.currentPage)
        Assertions.assertEquals(expectedPerPage, actualPage.perPage)
        Assertions.assertEquals(expectedTotal, actualPage.total)
        Assertions.assertEquals(expectedItemsCount, actualPage.items.size)
        Assertions.assertEquals(expectedGenreName, actualPage.items[0].name)
    }

    @ParameterizedTest
    @CsvSource("0,2,2,5,Ação;Comédia romântica", "1,2,2,5,Drama;Ficção científica", "2,2,1,5,Terror")
    open fun givenAValidSortAndDirection_whenCallsFindAll_shouldReturnFiltered(
        expectedPage: Int,
        expectedPerPage: Int,
        expectedItemsCount: Int,
        expectedTotal: Long,
        expectedGenres: String
    ) {
        mockGenres()
        val expectedTerms = ""
        val expectedSort = "name"
        val expectedDirection = "asc"
        val query = SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection)

        val actualPage = genreGateway.findAll(query)

        Assertions.assertEquals(expectedPage, actualPage.currentPage)
        Assertions.assertEquals(expectedPerPage, actualPage.perPage)
        Assertions.assertEquals(expectedTotal, actualPage.total)
        Assertions.assertEquals(expectedItemsCount, actualPage.items.size)
        for ((index, expectedName) in expectedGenres.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            .withIndex()) {
            val actualName = actualPage.items[index].name
            Assertions.assertEquals(expectedName, actualName)
        }
    }

    private fun mockGenres() {
        genreRepository.saveAllAndFlush(
            listOf(
                GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção científica", true))
            )
        )
    }

    private fun sorted(expectedCategories: List<CategoryID>) = expectedCategories.stream()
        .sorted(Comparator.comparing(CategoryID::value))
        .toList()
}