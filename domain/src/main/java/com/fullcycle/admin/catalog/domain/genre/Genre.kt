package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import java.time.Instant
import java.util.*


open class Genre protected constructor(
    val id: GenreID,
    val name: String?,
    val isActive: Boolean,
    val categories: List<CategoryID>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) : AggregateRoot<GenreID>(id) {

    override fun validate(handler: ValidationHandler) {}
    fun getCategories(): List<CategoryID> {
        return Collections.unmodifiableList(categories)
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