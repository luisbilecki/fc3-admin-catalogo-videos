package com.fullcycle.admin.catalog.infrastructure.category

import com.fullcycle.admin.catalog.infrastructure.MySQLGatewayTest
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


@MySQLGatewayTest
open class CategoryMySQLGatewayTest(
    private val categoryGateway: CategoryMySQLGateway,
    private val categoryRepository: CategoryRepository
) {

    @Test
    fun testInjectedDependencies() {
        Assertions.assertNotNull(categoryGateway)
        Assertions.assertNotNull(categoryRepository)
    }
}