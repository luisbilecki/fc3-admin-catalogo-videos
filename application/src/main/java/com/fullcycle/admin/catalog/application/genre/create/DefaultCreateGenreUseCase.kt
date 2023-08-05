package com.fullcycle.admin.catalog.application.genre.create

import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import java.util.*
import java.util.stream.Collectors


class DefaultCreateGenreUseCase(
    categoryGateway: CategoryGateway,
    genreGateway: GenreGateway
) : CreateGenreUseCase() {
    private val categoryGateway: CategoryGateway
    private val genreGateway: GenreGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
        this.genreGateway = Objects.requireNonNull(genreGateway)
    }

    override fun execute(command: CreateGenreCommand): CreateGenreOutput {
        val name = command.name
        val isActive = command.isActive
        val categories = toCategoryID(command.categories)
        val notification = Notification.create()
        notification.append(validateCategories(categories))

        val genre = notification.validate {
            Genre.newGenre(
                name,
                isActive
            )
        }

        if (notification.hasError()) {
            throw NotificationException("Could not create Aggregate Genre", notification)
        }

        genre?.addCategories(categories)

        return CreateGenreOutput.from(genreGateway.create(genre!!))
    }

    private fun validateCategories(ids: List<CategoryID>?): ValidationHandler {
        val notification = Notification.create()
        if (ids.isNullOrEmpty()) {
            return notification
        }

        val retrievedIds = categoryGateway.existsByIds(ids)

        if (ids.size != retrievedIds.size) {
            val missingIds = ArrayList(ids)
            missingIds.removeAll(retrievedIds)

            val missingIdsMessage = missingIds.stream()
                .map(CategoryID::value)
                .collect(Collectors.joining(", "))
            notification.append(Error("Some categories could not be found: %s".formatted(missingIdsMessage)))
        }

        return notification
    }

    private fun toCategoryID(categories: List<String>) = categories.stream()
        .map(CategoryID::from)
        .toList()
}