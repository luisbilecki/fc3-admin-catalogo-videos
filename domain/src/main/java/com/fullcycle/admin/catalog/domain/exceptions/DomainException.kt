package com.fullcycle.admin.catalog.domain.exceptions

import com.fullcycle.admin.catalog.domain.validation.Error


class DomainException private constructor(message: String?, val errors: List<Error>) : NoStackTraceException(message) {

    companion object {
        fun with(error: Error): DomainException {
            return DomainException(error.message, listOf(error))
        }

        fun with(errors: List<Error>): DomainException {
            return DomainException("", errors)
        }
    }
}