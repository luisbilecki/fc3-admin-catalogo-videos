package com.fullcycle.admin.catalog.domain.category

import java.time.Instant
import java.util.*

class Category private constructor(
        val id: String,
        val name: String,
        val description: String,
        val isActive: Boolean,
        val createdAt: Instant,
        val updatedAt: Instant,
        val deletedAt: Instant?
) {

    companion object {
        fun newCategory(aName: String, aDescription: String, aIsActive: Boolean): Category {
            val id = UUID.randomUUID().toString()
            val now = Instant.now()
            return Category(id, aName, aDescription, aIsActive, now, now, null)
        }
    }
}
