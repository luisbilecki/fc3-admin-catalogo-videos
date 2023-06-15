package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.validation.handler.ThrowsValidationHandler
import java.util.*


class DefaultCreateCategoryUseCase(categoryGateway: CategoryGateway) : CreateCategoryUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(command: CreateCategoryCommand): CreateCategoryOutput {
        val name = command.name
        val description = command.description
        val isActive = command.isActive
        val category = Category.newCategory(name, description, isActive)
        category.validate(ThrowsValidationHandler())
        return CreateCategoryOutput.from(categoryGateway.create(category))
    }
}