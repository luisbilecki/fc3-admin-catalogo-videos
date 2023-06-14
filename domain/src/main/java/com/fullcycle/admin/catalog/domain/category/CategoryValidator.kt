package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.ValidationHandler
import com.fullcycle.admin.catalog.domain.validation.Validator

class CategoryValidator(
        private val category: Category,
        validationHandler: ValidationHandler
) : Validator(validationHandler) {

    override fun validate() {
        if (category.name == null) {
            validationHandler().append(Error("'name' should not be null"))
        }
    }
}