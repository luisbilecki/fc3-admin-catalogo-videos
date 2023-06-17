package com.fullcycle.admin.catalog.application.category.update

import com.fullcycle.admin.catalog.application.UseCase
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import io.vavr.control.Either

abstract class UpdateCategoryUseCase : UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>>()