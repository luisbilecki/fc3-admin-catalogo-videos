package com.fullcycle.admin.catalog.infrastructure.api

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@RequestMapping(value = ["categories"])
@Tag(name = "Categories")
interface CategoryAPI {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new category")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Created successfully"),
        ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    ])
    fun createCategory(): ResponseEntity<*>?

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Listed successfully"),
        ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
        ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    ])
    fun listCategories(
        @RequestParam(name = "search", required = false, defaultValue = "") search: String?,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") perPage: Int,
        @RequestParam(name = "sort", required = false, defaultValue = "name") sort: String?,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") direction: String?
    ): Pagination<*>?
}