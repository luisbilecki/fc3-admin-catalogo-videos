package com.fullcycle.admin.catalog.infrastructure.api.controllers

import com.fullcycle.admin.catalog.application.category.create.CreateCategoryCommand
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryOutput
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalog.domain.pagination.Pagination
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.infrastructure.api.CategoryAPI
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryApiInput
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI;
import java.util.function.Function;


@RestController
class CategoryController(private val createCategoryUseCase: CreateCategoryUseCase) : CategoryAPI {

    override fun createCategory(input: CreateCategoryApiInput): ResponseEntity<*>? {
        val command = CreateCategoryCommand.with(
            input.name,
            input.description,
            input.active
        )

        val onError: Function<Notification, ResponseEntity<*>> =
            Function<Notification, ResponseEntity<*>> { notification ->
                ResponseEntity.unprocessableEntity().body(notification)
            }

        val onSuccess: Function<CreateCategoryOutput, ResponseEntity<*>> =
            Function<CreateCategoryOutput, ResponseEntity<*>> { output ->
                ResponseEntity.created(URI.create("/categories/" + output.id)).body(output)
            }

        return createCategoryUseCase.execute(command)
            .fold(onError, onSuccess)
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