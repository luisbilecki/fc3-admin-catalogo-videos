package com.fullcycle.admin.catalog.infrastructure.genre

import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.springframework.stereotype.Component


@Component
class GenreMySQLGateway(private val repository: GenreRepository) : GenreGateway {
    override fun create(genre: Genre) = save(genre)

    override fun deleteById(id: GenreID) {}

    override fun findById(id: GenreID) = null

    override fun update(genre: Genre) = save(genre)

    override fun findAll(query: SearchQuery) = Pagination<Genre>(0,0,0, emptyList())

    private fun save(genre: Genre) = repository
            .save(GenreJpaEntity.from(genre))
            .toAggregate()
}