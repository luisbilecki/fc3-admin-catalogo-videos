package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.Validator
import com.fullcycle.admin.catalog.domain.validation.Error


class GenreValidator(private val genre: Genre, handler: ValidationHandler) :
    Validator(handler) {

    companion object {
        const val NAME_MAX_LENGTH = 255
        const val NAME_MIN_LENGTH = 1
    }

    override fun validate() {
        checkNameConstraints()
    }

    private fun checkNameConstraints() {
        if (genre.name == null) {
            validationHandler().append(Error("'name' should not be null"))
            return
        }
        if (genre.name!!.isBlank()) {
            validationHandler().append(Error("'name' should not be empty"))
            return
        }
        val length = genre.name!!.trim { it <= ' ' }.length
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(Error("'name' must be between 1 and 255 characters"))
        }
    }
}