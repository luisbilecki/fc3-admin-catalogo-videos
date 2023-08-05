package com.fullcycle.admin.catalog.application.genre.retrieve.get

import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import java.util.*


class DefaultGetGenreByIdUseCase(genreGateway: GenreGateway) : GetGenreByIdUseCase() {
    private val genreGateway: GenreGateway

    init {
        this.genreGateway = Objects.requireNonNull(genreGateway)
    }

    override fun execute(id: String): GenreOutput {
        val genreId = GenreID.from(id)
        val foundGenre = genreGateway.findById(genreId) ?: throw NotFoundException.with(
            Genre::class.java,
            genreId
        )

        return GenreOutput.from(foundGenre)
    }
}