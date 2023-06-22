package com.fullcycle.admin.catalog.infrastructure.api.controllers

import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.exceptions.NotFoundException
import com.fullcycle.admin.catalog.domain.validation.Error
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(ex: DomainException): ResponseEntity<*> = ResponseEntity
        .unprocessableEntity()
        .body(APIError.from(ex))

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<*> = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(APIError.from(ex))
    
    data class APIError(val message: String?, val errors: List<Error>) {
        companion object {

            fun from(ex: DomainException): APIError {
                return APIError(ex.message, ex.errors)
            }
        }
    }
}