package com.fullcycle.admin.catalog.domain.validation


abstract class Validator protected constructor(
        private val handler: ValidationHandler
) {

    abstract fun validate()

    protected fun validationHandler() = handler
}