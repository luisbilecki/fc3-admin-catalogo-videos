package com.fullcycle.admin.catalog.application.genre.retrieve.list

import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import java.util.*

class DefaultListGenreUseCase(genreGateway: GenreGateway) : ListGenreUseCase() {
    private val genreGateway: GenreGateway

    init {
        this.genreGateway = Objects.requireNonNull(genreGateway)
    }

    override fun execute(query: SearchQuery) = genreGateway.findAll(query)
        .map(GenreListOutput::from)
}