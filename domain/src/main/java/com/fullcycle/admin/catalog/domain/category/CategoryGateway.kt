package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import java.util.*


interface CategoryGateway {

    fun create(category: Category): Category

    fun deleteById(id: CategoryID)

    fun findById(id: CategoryID): Category?

    fun update(category: Category): Category?

    fun findAll(query: CategorySearchQuery): Pagination<Category>
}