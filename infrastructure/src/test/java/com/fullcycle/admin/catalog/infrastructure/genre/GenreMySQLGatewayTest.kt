package com.fullcycle.admin.catalog.infrastructure.genre

import com.fullcycle.admin.catalog.infrastructure.category.CategoryMySQLGateway
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

open class GenreMySQLGatewayTest(
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
}