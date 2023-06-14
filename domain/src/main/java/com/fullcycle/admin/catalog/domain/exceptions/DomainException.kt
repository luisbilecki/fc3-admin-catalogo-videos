package com.fullcycle.admin.catalog.domain.exceptions

import com.fullcycle.admin.catalog.domain.validation.Error
import java.lang.RuntimeException

class DomainException private constructor(val errors: List<Error>) : RuntimeException("", null, true, false) {

    companion object {
        fun with(errors: List<Error>) = DomainException(errors)
    }
}