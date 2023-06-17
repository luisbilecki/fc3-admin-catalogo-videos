package com.fullcycle.admin.catalog.application.category.retrieve.get

import com.fullcycle.admin.catalog.application.UseCase

abstract class GetCategoryByIdUseCase : UseCase<String, CategoryOutput>()