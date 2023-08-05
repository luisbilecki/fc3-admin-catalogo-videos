package com.fullcycle.admin.catalog.application.genre.update

import com.fullcycle.admin.catalog.domain.genre.Genre

data class UpdateGenreOutput(val id: String) {

    companion object {

        fun from(genre: Genre) = UpdateGenreOutput(genre.id.value)
    }
}