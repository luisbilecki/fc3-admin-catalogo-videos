package com.fullcycle.admin.catalog.domain.exceptions

import com.fullcycle.admin.catalog.domain.AggregateRoot
import com.fullcycle.admin.catalog.domain.Identifier
import com.fullcycle.admin.catalog.domain.validation.Error


open class NotFoundException protected constructor(message: String?, errors: List<Error>) : DomainException(message, errors) {

    companion object {
        fun with(
            anAggregate: Class<out AggregateRoot<*>?>,
            id: Identifier
        ): NotFoundException {
            val error = String.format(
                "%s with ID %s was not found",
                anAggregate.simpleName,
                id.value
            )
            return NotFoundException(error, emptyList())
        }
    }
}