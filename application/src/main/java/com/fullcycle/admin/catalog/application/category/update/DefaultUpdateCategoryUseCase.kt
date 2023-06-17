package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import io.vavr.API.Left
import io.vavr.control.Either
import io.vavr.control.Try
import java.util.*


class DefaultUpdateCategoryUseCase(categoryGateway: CategoryGateway) : UpdateCategoryUseCase() {
    private val categoryGateway: CategoryGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
    }

    override fun execute(command: UpdateCategoryCommand): Either<Notification, UpdateCategoryOutput> {
        val id = CategoryID.from(command.id)
        val name = command.name
        val description = command.description
        val isActive = command.isActive
        val category = categoryGateway.findById(id) ?: throw notFound(id)
        val notification = Notification.create()

        category
                .update(name, description, isActive)
                .validate(notification)

        return if (notification.hasError()) Left(notification) else update(category)
    }

    private fun update(category: Category): Either<Notification, UpdateCategoryOutput> {
        return Try.of { categoryGateway.update(category) }
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from)
    }

    private fun notFound(id: CategoryID): DomainException {
        return DomainException.with(
            Error(String.format("Category with ID %s was not found", id.value))
        )
    }
}