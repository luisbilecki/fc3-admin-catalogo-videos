package com.fullcycle.admin.catalog.application.category.retrieve.list

import com.fullcycle.admin.catalog.application.UseCase
import com.fullcycle.admin.catalog.domain.category.CategorySearchQuery
import com.fullcycle.admin.catalog.domain.pagination.Pagination

abstract class ListCategoriesUseCase : UseCase<CategorySearchQuery, Pagination<CategoryListOutput>>()