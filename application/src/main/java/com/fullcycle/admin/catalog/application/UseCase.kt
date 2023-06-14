package com.fullcycle.admin.catalog.application

import com.fullcycle.admin.catalog.domain.category.Category

class UseCase {
    fun execute(): Category {
        return Category.newCategory("", "", false)
    }
}