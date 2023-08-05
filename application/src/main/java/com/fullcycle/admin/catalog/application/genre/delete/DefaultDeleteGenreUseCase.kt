package com.fullcycle.admin.catalog.application.genre.delete

import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import java.util.*

class DefaultDeleteGenreUseCase(genreGateway: GenreGateway) : DeleteGenreUseCase() {
    private val genreGateway: GenreGateway

    init {
        this.genreGateway = Objects.requireNonNull(genreGateway)
    }

    override fun execute(id: String) {
        genreGateway.deleteById(GenreID.from(id))
    }
}