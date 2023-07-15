package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.application.UseCase
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery
import com.fullcycle.admin.catalog.domain.pagination.Pagination

abstract class ListCategoriesUseCase : UseCase<SearchQuery, Pagination<CategoryListOutput>>()