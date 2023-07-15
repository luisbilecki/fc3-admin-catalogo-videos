package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.domain.pagination.Pagination

interface CategoryGateway {

    fun create(category: Category?): Category?

    fun deleteById(id: CategoryID)

    fun findById(id: CategoryID): Category?

    fun update(category: Category?): Category?

    fun findAll(query: SearchQuery): Pagination<Category>
}