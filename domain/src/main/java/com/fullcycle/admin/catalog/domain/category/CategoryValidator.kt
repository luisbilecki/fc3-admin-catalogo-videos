package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.Validator

class CategoryValidator(
        private val category: Category,
        validationHandler: ValidationHandler
) : Validator(validationHandler) {

    companion object {
        const val NAME_MAX_LENGTH = 255
        const val NAME_MIN_LENGTH = 3
    }

    override fun validate() {
        checkNameConstraints()
    }

    private fun checkNameConstraints() {
        val name = category.name

        if (name == null) {
            validationHandler().append(Error("'name' should not be null"))
            return;
        }

        if (name.isBlank()) {
            validationHandler().append(Error("'name' should not be empty"))
            return;
        }

        val length = name.trim().length
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(Error("'name' must be between 3 and 255 characters"))
        }
    }
}