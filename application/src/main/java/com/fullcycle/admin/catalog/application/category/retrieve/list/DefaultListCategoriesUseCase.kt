package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategorySearchQuery
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import java.util.*


class DefaultListCategoriesUseCase(categoryGateway: CategoryGateway) : ListCategoriesUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(input: CategorySearchQuery): Pagination<CategoryListOutput> {
        return categoryGateway.findAll(input).map(CategoryListOutput::from)
    }
}