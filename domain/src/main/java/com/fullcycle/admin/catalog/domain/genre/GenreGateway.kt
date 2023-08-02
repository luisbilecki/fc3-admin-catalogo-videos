package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery

interface GenreGateway {

    fun create(genre: Genre): Genre
    fun deleteById(id: GenreID)
    fun findById(id: GenreID): Genre?
    fun update(genre: Genre): Genre?
    fun findAll(query: SearchQuery): Pagination<Genre>
}