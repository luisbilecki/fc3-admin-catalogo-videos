package com.fullcycle.admin.catalog.application.genre.create

import com.fullcycle.admin.catalog.domain.genre.Genre

data class CreateGenreOutput(val id: String) {

    companion object {

        fun from(id: String) = CreateGenreOutput(id)

        fun from(genre: Genre) = CreateGenreOutput(genre.id.value)
    }
}