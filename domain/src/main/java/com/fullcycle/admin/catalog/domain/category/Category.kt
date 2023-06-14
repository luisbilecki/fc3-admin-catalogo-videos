package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.AggregateRoot
import java.time.Instant
import java.util.*

class Category private constructor(
        val id: CategoryID,
        val name: String,
        val description: String,
        val isActive: Boolean,
        val createdAt: Instant,
        val updatedAt: Instant,
        val deletedAt: Instant?
) : AggregateRoot<CategoryID>(id) {

    companion object {
        fun newCategory(aName: String, aDescription: String, aIsActive: Boolean): Category {
            val id = CategoryID.unique()
            val now = Instant.now()
            return Category(id, aName, aDescription, aIsActive, now, now, null)
        }
    }
}
