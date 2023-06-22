package com.fullcycle.admin.catalog.infrastructure.api.controllers

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.infrastructure.api.CategoryAPI
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryController : CategoryAPI {

    override fun createCategory(): ResponseEntity<*>? {
        return null
    }

    override fun listCategories(
        search: String?,
        page: Int,
        perPage: Int,
        sort: String?,
        direction: String?
    ): Pagination<*>? {
        return null
    }
}