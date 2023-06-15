package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import java.time.Instant
import java.util.*

class Category private constructor(
        val id: CategoryID,
        val name: String?,
        val description: String,
        val isActive: Boolean,
        val createdAt: Instant,
        val updatedAt: Instant,
        val deletedAt: Instant?
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
}
