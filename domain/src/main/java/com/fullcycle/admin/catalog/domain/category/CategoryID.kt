package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.Identifier
import java.util.*

class CategoryID private constructor(override val value: String) : Identifier() {

    companion object {

        fun unique() = from(UUID.randomUUID())
        fun from(id: String) = CategoryID(id)
        fun from(id: UUID) = CategoryID(id.toString().lowercase(Locale.getDefault()))
    }
}