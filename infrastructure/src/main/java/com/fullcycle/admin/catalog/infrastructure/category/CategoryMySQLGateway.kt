package com.fullcycle.admin.catalog.infrastructure.category

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalog.infrastructure.utils.SpecificationUtils.like
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.StreamSupport


@Component
class CategoryMySQLGateway(private val repository: CategoryRepository) : CategoryGateway {

    override fun create(category: Category?) = save(category)

    override fun deleteById(id: CategoryID) {
        val idToDelete = id.value
        if (repository.existsById(idToDelete)) {
            repository.deleteById(idToDelete)
        }
    }

    override fun findById(id: CategoryID): Category? = repository
        .findById(id.value)
        .map(CategoryJpaEntity::toAggregate)
        .orElse(null)

    override fun existsByIds(categoryIDs: Iterable<CategoryID>): List<CategoryID>{
        val ids = StreamSupport.stream(categoryIDs.spliterator(), false)
            .map(CategoryID::value)
            .toList()
        return repository.existsByIds(ids).stream()
            .map(CategoryID::from)
            .toList()
    }

    override fun update(category: Category?) = save(category)

    override fun findAll(query: SearchQuery): Pagination<Category> {
        val page: PageRequest = PageRequest.of(
            query.page,
            query.perPage,
            Sort.by(Sort.Direction.fromString(query.direction), query.sort)
        )

        val specifications = Optional.ofNullable(query.terms)
            .filter { str -> str.isNotBlank() }
            .map(::assembleSpecification)
            .orElse(null)

        val pageResult = repository.findAll(Specification.where(specifications), page)

        return Pagination(
            pageResult.number,
            pageResult.size,
            pageResult.totalElements,
            pageResult.map { obj: CategoryJpaEntity -> obj.toAggregate() }.toList()
        )
    }

    private fun save(category: Category?): Category? {
        if (category == null) {
            return null
        }
        return repository
            .save(CategoryJpaEntity.from(category))
            .toAggregate()
    }

    private fun assembleSpecification(str: String): Specification<CategoryJpaEntity>? {
        val nameLike: Specification<CategoryJpaEntity> = like("name", str)
        val descriptionLike: Specification<CategoryJpaEntity> = like("description", str)
        return nameLike.or(descriptionLike)
    }
}