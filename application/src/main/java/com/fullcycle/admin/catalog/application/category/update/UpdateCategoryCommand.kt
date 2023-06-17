package com.fullcycle.admin.catalog.application.category.update


data class UpdateCategoryCommand(val id: String, val name: String?, val description: String, val isActive: Boolean) {

    companion object {

        fun with(id: String, name: String?, description: String, isActive: Boolean) =
                UpdateCategoryCommand(id, name, description, isActive)
    }
}