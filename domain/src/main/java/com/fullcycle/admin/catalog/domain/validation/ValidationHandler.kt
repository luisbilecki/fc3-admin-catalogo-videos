package com.fullcycle.admin.catalog.domain.validation

interface ValidationHandler {

    fun append(error: Error): ValidationHandler

    fun append(handler: ValidationHandler): ValidationHandler

    fun validate(validation: Validation): ValidationHandler

    fun getErrors(): List<Error>

}

fun ValidationHandler.hasError(): Boolean {
    return getErrors() != null && !getErrors().isEmpty();
}

interface Validation {

    fun validate()
}