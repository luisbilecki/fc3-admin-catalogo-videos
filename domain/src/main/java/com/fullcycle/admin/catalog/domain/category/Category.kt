package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import java.time.Instant
import java.util.*

class Category private constructor(
        val id: CategoryID,
        val name: String?,
        val description: String,
        var isActive: Boolean,
        val createdAt: Instant,
        var updatedAt: Instant,
        var deletedAt: Instant?
) : AggregateRoot<CategoryID>(id) {

    companion object {
        fun newCategory(name: String?, description: String, isActive: Boolean): Category {
            val id = CategoryID.unique()
            val now = Instant.now()
            val deletedAt = if (isActive) null else now
            return Category(id, name, description, isActive, now, now, deletedAt)
        }
    }

    override fun validate(handler: ValidationHandler) = CategoryValidator(this, handler).validate()

    fun activate(): Category {
        deletedAt = null
        isActive = true
        updatedAt = Instant.now()
        return this
    }

    fun deactivate(): Category {
        if (deletedAt == null) {
            deletedAt = Instant.now()
        }
        isActive = false
        updatedAt = Instant.now()
        return this
    }
}
