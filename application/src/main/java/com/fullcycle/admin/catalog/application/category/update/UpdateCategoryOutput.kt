package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.domain.category.Category


data class UpdateCategoryOutput(val id: String) {

    companion object {
        fun from(id: String?): UpdateCategoryOutput? {
            return UpdateCategoryOutput(id!!)
        }

        fun from(category: Category?): UpdateCategoryOutput {
            return UpdateCategoryOutput(category!!.id.value)
        }
    }
}