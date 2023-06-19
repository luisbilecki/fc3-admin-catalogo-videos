package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import java.time.Instant

data class CategoryListOutput(val id: CategoryID, val name: String?, val description: String?, val isActive: Boolean, val createdAt: Instant, val deletedAt: Instant?) {

    companion object {
        fun from(category: Category): CategoryListOutput {
            return CategoryListOutput(
                category.id,
                category.name,
                category.description,
                category.isActive,
                category.createdAt,
                category.deletedAt
            )
        }
    }
}