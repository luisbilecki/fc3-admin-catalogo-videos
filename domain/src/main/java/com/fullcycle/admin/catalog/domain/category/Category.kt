package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import java.time.Instant


class Category private constructor(
        val id: CategoryID,
        var name: String?,
        var description: String?,
        var isActive: Boolean,
        val createdAt: Instant,
        var updatedAt: Instant,
        var deletedAt: Instant?
) : AggregateRoot<CategoryID>(id), Cloneable {

    companion object {
        fun newCategory(name: String?, description: String?, isActive: Boolean): Category {
            val id = CategoryID.unique()
            val now = Instant.now()
            val deletedAt = if (isActive) null else now
            return Category(id, name, description, isActive, now, now, deletedAt)
        }

        fun with(
                id: CategoryID,
                name: String?,
                description: String?,
                active: Boolean,
                createdAt: Instant?,
                updatedAt: Instant?,
                deletedAt: Instant?
        ) = Category(
                id,
                name,
                description,
                active,
                createdAt!!,
                updatedAt!!,
                deletedAt
        )

        fun with(category: Category) = Category(
                category.id,
                category.name,
                category.description,
                category.isActive,
                category.createdAt,
                category.updatedAt,
                category.deletedAt
        )
    }

    override fun validate(handler: ValidationHandler) = CategoryValidator(this, handler).validate()

    override fun clone(): Category {
        return try {
            super.clone() as Category
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }

    fun activate(): Category {
        this.deletedAt = null
        this.isActive = true
        this.updatedAt = Instant.now()
        return this
    }

    fun deactivate(): Category {
        if (this.deletedAt == null) {
            this.deletedAt = Instant.now()
        }
        this.isActive = false
        this.updatedAt = Instant.now()
        return this
    }

    fun update(
            name: String?,
            description: String,
            isActive: Boolean
    ): Category {
        if (isActive) {
            activate()
        } else {
            deactivate()
        }
        this.name = name
        this.description = description
        this.updatedAt = Instant.now()
        return this
    }
}
