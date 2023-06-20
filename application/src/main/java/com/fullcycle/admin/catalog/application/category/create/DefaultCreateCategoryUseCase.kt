package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import io.vavr.API.Left
import io.vavr.control.Either
import io.vavr.control.Try
import java.util.*


class DefaultCreateCategoryUseCase(categoryGateway: CategoryGateway) : CreateCategoryUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(input: CreateCategoryCommand): Either<Notification, CreateCategoryOutput> {
        val name = input.name
        val description = input.description
        val isActive = input.isActive
        val category = Category.newCategory(name, description, isActive)
        val notification = Notification.create()

        category.validate(notification)

        return if (notification.hasError()) Left(notification) else create(category)
    }

    private fun create(category: Category): Either<Notification, CreateCategoryOutput> {
        return Try.of { categoryGateway.create(category) }
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from)
    }
}