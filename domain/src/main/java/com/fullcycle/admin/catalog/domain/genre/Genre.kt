package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import java.time.Instant
import java.util.*


class Genre private constructor(
    val id: GenreID,
    val name: String?,
    val isActive: Boolean,
    val categories: List<CategoryID>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) : AggregateRoot<GenreID>(id) {

    init {
        val notification = Notification.create()
        validate(notification)

        if (notification.hasError()) {
            throw NotificationException("Failed to create a Aggregate Genre", notification)
        }
    }

    override fun validate(handler: ValidationHandler) {
        GenreValidator(this, handler).validate()
    }

    companion object {
        fun newGenre(name: String?, isActive: Boolean): Genre {
            val id = GenreID.unique()
            val now = Instant.now()
            val deletedAt = if (isActive) null else now

            return Genre(id, name, isActive, ArrayList(), now, now, deletedAt)
        }

        fun with(
            id: GenreID,
            name: String,
            isActive: Boolean,
            categories: List<CategoryID>,
            createdAt: Instant,
            updatedAt: Instant,
            deletedAt: Instant?
        ) = Genre(id, name, isActive, categories, createdAt, updatedAt, deletedAt)

        fun with(genre: Genre) = Genre(
            genre.id,
            genre.name,
            genre.isActive,
            genre.categories,
            genre.createdAt,
            genre.updatedAt,
            genre.deletedAt
        )
    }
}