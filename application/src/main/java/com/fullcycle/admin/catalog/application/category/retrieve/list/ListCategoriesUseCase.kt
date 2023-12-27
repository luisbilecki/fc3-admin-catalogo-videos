package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.application.UseCase
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery

abstract class ListCategoriesUseCase : UseCase<SearchQuery, Pagination<CategoryListOutput>>()