package com.fullcycle.admin.catalog.domain.validation.handler

import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.Validation
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler

class Notification private constructor(private val errors: MutableList<Error>) : ValidationHandler {

    override fun append(error: Error): Notification {
        errors.add(error)
        return this
    }

    override fun append(handler: ValidationHandler): Notification {
        errors.addAll(handler.getErrors())
        return this
    }

    override fun validate(validation: Validation): Notification {
        try {
            validation.validate()
        } catch (ex: DomainException) {
            errors.addAll(ex.errors)
        } catch (t: Throwable) {
            errors.add(Error(t.message ?: ""))
        }
        return this
    }

    override fun getErrors(): List<Error> {
        return errors
    }

    companion object {
        fun create(): Notification {
            return Notification(ArrayList())
        }

        fun create(error: Error): Notification {
            return Notification(ArrayList()).append(error)
        }

        fun create(t: Throwable): Notification {
            return create(Error(t.message!!))
        }
    }
}