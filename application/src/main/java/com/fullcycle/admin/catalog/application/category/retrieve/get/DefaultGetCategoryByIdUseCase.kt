package com.fullcycle.admin.catalog.application.category.retrieve.get

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalog.domain.validation.Error
import java.util.*


class DefaultGetCategoryByIdUseCase(categoryGateway: CategoryGateway) : GetCategoryByIdUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(input: String): CategoryOutput {
        val categoryId = CategoryID.from(input)
        val foundCategory = categoryGateway.findById(categoryId) ?: throw notFound(categoryId)

        return CategoryOutput.from(foundCategory)
    }

    private fun notFound(id: CategoryID): DomainException {
        return NotFoundException.with(Category::class.java, id)
    }
}