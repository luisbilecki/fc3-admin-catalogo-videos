package com.fullcycle.admin.catalog.domain.validation.handler

import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.Validation
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler


class ThrowsValidationHandler : ValidationHandler {
    override fun append(error: Error) = throw DomainException.with(listOf(error))

    override fun append(handler: ValidationHandler) = throw DomainException.with(getErrors())

    override fun <T> validate(validation: Validation<T>): T {
        return try {
            validation.validate()
        } catch (ex: Exception) {
            throw DomainException.with(listOf(
                    Error(ex.message!!)
            ))
        }
    }

    override fun getErrors(): List<Error> = listOf()
}