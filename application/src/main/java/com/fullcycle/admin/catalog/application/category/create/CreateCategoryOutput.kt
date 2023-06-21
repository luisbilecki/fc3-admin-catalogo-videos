package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.domain.category.Category

data class CreateCategoryOutput(val id: String) {

    companion object {

        fun from(id: String): CreateCategoryOutput {
            return CreateCategoryOutput(id)
        }

        fun from(category: Category?): CreateCategoryOutput {
            return CreateCategoryOutput(category!!.id.value)
        }
    }
}