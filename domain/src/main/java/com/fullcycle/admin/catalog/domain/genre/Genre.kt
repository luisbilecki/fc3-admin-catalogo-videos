package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.NotificationException
import com.fullcycle.admin.catalog.domain.utils.InstantUtils.now
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.domain.validation.hasError
import java.time.Instant


class Genre private constructor(
    val id: GenreID,
    var name: String?,
    var isActive: Boolean,
    var categories: List<CategoryID>,
    val createdAt: Instant,
    var updatedAt: Instant,
    var deletedAt: Instant?
) : AggregateRoot<GenreID>(id) {

    init {
        selfValidate()
    }

    override fun validate(handler: ValidationHandler) {
        GenreValidator(this, handler).validate()
    }

    private fun selfValidate() {
        val notification = Notification.create()
        validate(notification)

        if (notification.hasError()) {
            throw NotificationException("Failed to create a Aggregate Genre", notification)
        }
    }

    fun deactivate(): Genre? {
        if (deletedAt == null) {
            deletedAt = now()
        }
        isActive = false
        updatedAt = now()
        return this
    }

    fun activate(): Genre? {
        deletedAt = null
        isActive = true
        updatedAt = now()
        return this
    }

    fun update(newName: String?, isActive: Boolean, newCategories: List<CategoryID>?): Genre? {
        if (isActive) {
            activate()
        } else {
            deactivate()
        }
        name = newName
        categories = newCategories ?: emptyList()
        updatedAt = now()
        selfValidate()
        return this
    }

    companion object {
        fun newGenre(name: String?, isActive: Boolean): Genre {
            val id = GenreID.unique()
            val now = now()
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