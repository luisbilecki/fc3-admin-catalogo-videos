package com.fullcycle.admin.catalog.application.category.delete

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import java.util.*


class DefaultDeleteCategoryUseCase(categoryGateway: CategoryGateway) : DeleteCategoryUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(input: String) {
        categoryGateway.deleteById(CategoryID.from(input))
    }
}