package com.fullcycle.admin.catalog.infrastructure.api

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@RequestMapping(value = ["categories"])
interface CategoryAPI {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createCategory(): ResponseEntity<*>?

    @GetMapping
    fun listCategories(
        @RequestParam(name = "search", required = false, defaultValue = "") search: String?,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") perPage: Int,
        @RequestParam(name = "sort", required = false, defaultValue = "name") sort: String?,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") direction: String?
    ): Pagination<*>?
}