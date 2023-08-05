package com.fullcycle.admin.catalog.domain.validation

interface ValidationHandler {

    fun append(error: Error): ValidationHandler

    fun append(handler: ValidationHandler): ValidationHandler

    fun <T> validate(aValidation: Validation<T>): T?

    fun getErrors(): List<Error>

}

fun ValidationHandler.hasError(): Boolean {
    return getErrors().isNotEmpty()
}

fun ValidationHandler.firstError(): Error? {
    return if (hasError()) {
        getErrors().first()
    } else {
        null
    }
}

fun interface Validation<T> {

    fun validate(): T
}