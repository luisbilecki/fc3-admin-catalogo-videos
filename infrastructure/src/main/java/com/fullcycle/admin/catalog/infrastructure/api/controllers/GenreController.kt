package com.fullcycle.admin.catalog.infrastructure.api.controllers

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.infrastructure.api.GenreAPI
import com.fullcycle.admin.catalog.infrastructure.genre.models.CreateGenreRequest
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreListResponse
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreResponse
import com.fullcycle.admin.catalog.infrastructure.genre.models.UpdateGenreRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController


@RestController
class GenreController : GenreAPI {
    override fun create(input: CreateGenreRequest?): ResponseEntity<*>? {
        return null
    }

    override fun list(
        search: String?,
        page: Int,
        perPage: Int,
        sort: String?,
        direction: String?
    ): Pagination<GenreListResponse?>? {
        return null
    }

    override fun getById(id: String?): GenreResponse? {
        return null
    }

    override fun updateById(id: String?, input: UpdateGenreRequest?): ResponseEntity<*>? {
        return null
    }

    override fun deleteById(id: String?) {}
}