package com.fullcycle.admin.catalog.infrastructure.category.presenters

import com.fullcycle.admin.catalog.application.category.retrieve.get.CategoryOutput
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryAPIOutput


interface CategoryAPIPresenter {

    companion object {

        fun present(output: CategoryOutput) = CategoryAPIOutput(
            output.id.value,
            output.name,
            output.description,
            output.isActive,
            output.createdAt,
            output.updatedAt,
            output.deletedAt
        )
    }
}