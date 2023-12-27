package com.fullcycle.admin.catalog.infrastructure.api

import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.infrastructure.genre.models.CreateGenreRequest
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreListResponse
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreResponse
import com.fullcycle.admin.catalog.infrastructure.genre.models.UpdateGenreRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(value = ["genres"])
@Tag(name = "Genre")
interface GenreAPI {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Create a new genre")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Created successfully"
        ), ApiResponse(
            responseCode = "422",
            description = "A validation error was thrown"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun create(@RequestBody input: CreateGenreRequest?): ResponseEntity<*>?

    @GetMapping
    @Operation(summary = "List all genres paginated")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Listed successfully"
        ), ApiResponse(
            responseCode = "422",
            description = "A invalid parameter was received"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun list(
        @RequestParam(name = "search", required = false, defaultValue = "") search: String?,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") perPage: Int,
        @RequestParam(name = "sort", required = false, defaultValue = "name") sort: String?,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") direction: String?
    ): Pagination<GenreListResponse?>?

    @GetMapping(value = ["{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = "Get a genre by it's identifier")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Genre retrieved successfully"), ApiResponse(
            responseCode = "404",
            description = "Genre was not found"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun getById(@PathVariable(name = "id") id: String?): GenreResponse?

    @PutMapping(
        value = ["{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(summary = "Update a genre by it's identifier")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Genre updated successfully"
        ), ApiResponse(
            responseCode = "404",
            description = "Genre was not found"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun updateById(@PathVariable(name = "id") id: String?, @RequestBody input: UpdateGenreRequest?): ResponseEntity<*>?

    @DeleteMapping(value = ["{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a genre by it's identifier")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "Genre deleted successfully"
        ), ApiResponse(
            responseCode = "404",
            description = "Genre was not found"
        ), ApiResponse(responseCode = "500", description = "An internal server error was thrown")]
    )
    fun deleteById(@PathVariable(name = "id") id: String?)
}