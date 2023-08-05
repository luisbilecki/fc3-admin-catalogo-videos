package com.fullcycle.admin.catalog.application.genre.retrieve.list

import com.fullcycle.admin.catalog.application.UseCase
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery

abstract class ListGenreUseCase : UseCase<SearchQuery, Pagination<GenreListOutput>>()