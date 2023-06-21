package com.fullcycle.admin.catalog.application

import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalog.IntegrationTest
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class SampleIT @Autowired constructor(
    private val useCase: CreateCategoryUseCase,
    private val categoryRepository: CategoryRepository
) {

    @Test
    fun testInjects() {
        Assertions.assertNotNull(useCase)
        Assertions.assertNotNull(categoryRepository)
    }
}