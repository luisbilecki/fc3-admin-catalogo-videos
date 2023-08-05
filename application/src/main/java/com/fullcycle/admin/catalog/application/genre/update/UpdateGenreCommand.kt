package com.fullcycle.admin.catalog.application.genre.update

data class UpdateGenreCommand(val id: String, val name: String?, val isActive: Boolean, val categories: List<String>) {

    companion object {

        fun with(
            id: String,
            name: String?,
            isActive: Boolean?,
            categories: List<String>
        ) = UpdateGenreCommand(id, name, isActive ?: true, categories)
    }
}