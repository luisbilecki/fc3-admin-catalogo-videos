package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID


data class UpdateCategoryOutput(val id: CategoryID) {

    companion object {

        fun from(category: Category?): UpdateCategoryOutput {
            return UpdateCategoryOutput(category!!.id)
        }
    }
}