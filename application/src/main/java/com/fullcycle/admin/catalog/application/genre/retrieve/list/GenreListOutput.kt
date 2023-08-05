package com.fullcycle.admin.catalog.application.genre.retrieve.list

import com.fullcycle.admin.catalog.domain.category.CategoryID

import com.fullcycle.admin.catalog.domain.genre.Genre

import java.time.Instant

data class GenreListOutput(
    val name: String?,
    val isActive: Boolean,
    val categories: List<String>,
    val createdAt: Instant,
    val deletedAt: Instant?
) {

    companion object {

        fun from(genre: Genre): GenreListOutput {
            return GenreListOutput(
                genre.name,
                genre.isActive,
                genre.categories.stream()
                    .map(CategoryID::value)
                    .toList(),
                genre.createdAt,
                genre.deletedAt
            )
        }
    }
}