package com.fullcycle.admin.catalog.application.genre.retrieve.get

import com.fullcycle.admin.catalog.domain.category.CategoryID

import com.fullcycle.admin.catalog.domain.genre.Genre

import java.time.Instant

data class GenreOutput(
    val id: String,
    val name: String?,
    val isActive: Boolean,
    val categories: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Instant?
) {

    companion object {

        fun from(aGenre: Genre) = GenreOutput(
            aGenre.id.value,
            aGenre.name,
            aGenre.isActive,
            aGenre.categories.stream()
                .map(CategoryID::value)
                .toList(),
            aGenre.createdAt,
            aGenre.updatedAt,
            aGenre.deletedAt
        )
    }
}