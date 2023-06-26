package com.fullcycle.admin.catalog.infrastructure.category.presenters

import com.fullcycle.admin.catalog.application.category.retrieve.get.CategoryOutput
import com.fullcycle.admin.catalog.application.category.retrieve.list.CategoryListOutput
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryListResponse
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse


interface CategoryAPIPresenter {

    companion object {

        fun present(output: CategoryOutput) = CategoryResponse(
            output.id.value,
            output.name,
            output.description,
            output.isActive,
            output.createdAt,
            output.updatedAt,
            output.deletedAt
        )

        fun present(output: CategoryListOutput): CategoryListResponse? {
            return CategoryListResponse(
                output.id.value,
                output.name,
                output.description,
                output.isActive,
                output.createdAt,
                output.deletedAt
            )
        }
    }
}