package com.fullcycle.admin.catalog.infrastructure.genre

import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import com.fullcycle.admin.catalog.domain.genre.GenreID
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import com.fullcycle.admin.catalog.infrastructure.utils.SpecificationUtils.like
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.stereotype.Component
import java.util.*


@Component
class GenreMySQLGateway(private val repository: GenreRepository) : GenreGateway {

    override fun create(genre: Genre) = save(genre)

    override fun deleteById(id: GenreID) {
        val idValue = id.value
        if (repository.existsById(idValue)) {
            repository.deleteById(idValue)
        }
    }

    override fun findById(id: GenreID): Genre? = repository
        .findById(id.value)
        .map(GenreJpaEntity::toAggregate)
        .orElse(null)

    override fun update(genre: Genre) = save(genre)

    override fun findAll(query: SearchQuery): Pagination<Genre> {
        val page = PageRequest.of(
            query.page,
            query.perPage,
            Sort.by(Sort.Direction.fromString(query.direction), query.sort)
        )

        val where = Optional.ofNullable(query.terms)
            .filter { str -> str != null }
            .map(::assembleSpecification)
            .orElse(null)

        val pageResult = repository.findAll(where(where), page)

        return Pagination(
            pageResult.number,
            pageResult.size,
            pageResult.totalElements,
            pageResult.map { obj: GenreJpaEntity -> obj.toAggregate() }.toList()
        )
    }

    private fun save(genre: Genre) = repository
        .save(GenreJpaEntity.from(genre))
        .toAggregate()

    private fun assembleSpecification(terms: String): Specification<GenreJpaEntity> = like("name", terms)
}