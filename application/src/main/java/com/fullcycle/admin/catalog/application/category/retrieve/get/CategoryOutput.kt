package com.fullcycle.admin.catalog.application.category.retrieve.get

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import java.time.Instant


data class CategoryOutput(val id: CategoryID, val name: String?, val description: String?, val isActive: Boolean, val createdAt: Instant, val updatedAt: Instant, val deletedAt: Instant?) {
    companion object {
        fun from(category: Category): CategoryOutput {
            return CategoryOutput(
                    category.id,
                    category.name,
                    category.description,
                    category.isActive,
                    category.createdAt,
                    category.updatedAt,
                    category.deletedAt
            )
        }
    }
}