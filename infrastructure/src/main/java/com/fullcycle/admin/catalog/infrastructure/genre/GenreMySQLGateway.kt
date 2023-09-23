package com.fullcycle.admin.catalog.infrastructure.genre

import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import org.springframework.stereotype.Component

@Component
class GenreMySQLGateway : GenreGateway {
    override fun create(genre: Genre): Genre = Genre.newGenre("test", false)

    override fun deleteById(id: GenreID) {}
    override fun findById(id: GenreID) = null

    override fun update(genre: Genre): Genre? {
        return null
    }

    override fun findAll(query: SearchQuery) = Pagination<Genre>(0,0,0, emptyList())
}