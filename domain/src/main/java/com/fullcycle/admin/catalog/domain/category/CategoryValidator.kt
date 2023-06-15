package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.Validator

class CategoryValidator(
        private val category: Category,
        validationHandler: ValidationHandler
) : Validator(validationHandler) {

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
        if (length > 255 || length < 3) {
            validationHandler().append(Error("'name' must be between 3 and 255 characters"))
        }


    }
}