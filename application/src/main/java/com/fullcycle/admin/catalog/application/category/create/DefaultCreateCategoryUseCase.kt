package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import io.vavr.API.Left
import io.vavr.control.Either
import io.vavr.control.Either.Left
import io.vavr.control.Try
import java.util.*


class DefaultCreateCategoryUseCase(categoryGateway: CategoryGateway) : CreateCategoryUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(command: CreateCategoryCommand): Either<Notification, CreateCategoryOutput> {
        val name = command.name
        val description = command.description
        val isActive = command.isActive
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