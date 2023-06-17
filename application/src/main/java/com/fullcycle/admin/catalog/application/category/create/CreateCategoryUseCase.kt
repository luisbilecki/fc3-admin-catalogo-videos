package com.fullcycle.admin.catalog.application.category.create

import com.fullcycle.admin.catalog.application.UseCase
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import io.vavr.control.Either

abstract class CreateCategoryUseCase : UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>>()