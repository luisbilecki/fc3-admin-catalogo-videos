package com.fullcycle.admin.catalog.application.genre.create


data class CreateGenreCommand(val name: String, val isActive: Boolean, val categories: List<String>) {

    companion object {

        fun with(
            name: String,
            isActive: Boolean?,
            categories: List<String>
        ) = CreateGenreCommand(name, isActive ?: true, categories)
    }
}