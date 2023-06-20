package com.fullcycle.admin.catalog.infrastructure.category

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.category.CategorySearchQuery
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.springframework.stereotype.Service


@Service
class CategoryMySQLGateway(private val repository: CategoryRepository) : CategoryGateway {

    override fun create(category: Category) = save(category)

    override fun deleteById(anId: CategoryID) {}
    override fun findById(anId: CategoryID): Category? {
        return null
    }

    override fun update(category: Category) = save(category)

    override fun findAll(query: CategorySearchQuery): Pagination<Category> {
        return Pagination(0, 0, 0, emptyList())
    }

    private fun save(category: Category) = repository
        .save(CategoryJpaEntity.from(category))
        .toAggregate()
}