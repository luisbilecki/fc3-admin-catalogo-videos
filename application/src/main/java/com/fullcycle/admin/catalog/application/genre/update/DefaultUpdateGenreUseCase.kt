package com.fullcycle.admin.catalog.application.genre.update

import com.fullcycle.admin.catalog.domain.Identifier
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import java.util.*
import java.util.stream.Collectors

class DefaultUpdateGenreUseCase(
    categoryGateway: CategoryGateway,
    genreGateway: GenreGateway
) : UpdateGenreUseCase() {
    private val categoryGateway: CategoryGateway
    private val genreGateway: GenreGateway

    init {
        this.categoryGateway = Objects.requireNonNull(categoryGateway)
        this.genreGateway = Objects.requireNonNull(genreGateway)
    }

    override fun execute(command: UpdateGenreCommand): UpdateGenreOutput {
        val id = GenreID.from(command.id)
        val name = command.name
        val isActive = command.isActive
        val categories = toCategoryId(command.categories)
        val genre = genreGateway.findById(id) ?: throw notFound(id)

        val notification = Notification.create()
        notification.append(validateCategories(categories))
        notification.validate { genre.update(name, isActive, categories) }

        if (notification.hasError()) {
            throw NotificationException(
                "Could not update Aggregate Genre %s".formatted(command.id), notification
            )
        }

        return UpdateGenreOutput.from(genreGateway.update(genre)!!)
    }

    private fun validateCategories(ids: List<CategoryID>): ValidationHandler {
        val notification = Notification.create()

        if (ids.isNullOrEmpty()) {
            return notification
        }

        val retrievedIds = categoryGateway.existsByIds(ids)

        if (ids.size != retrievedIds.size) {
            val missingIds = ArrayList(ids)
            missingIds.removeAll(retrievedIds.toSet())

            val missingIdsMessage = missingIds.stream()
                .map(CategoryID::value)
                .collect(Collectors.joining(", "))

            notification.append(Error("Some categories could not be found: %s".formatted(missingIdsMessage)))
        }

        return notification
    }

    private fun notFound(id: Identifier) = NotFoundException.with(Genre::class.java, id)

    private fun toCategoryId(categories: List<String>): MutableList<CategoryID> {
        return categories.stream()
            .map(CategoryID::from)
            .toList()
    }
}