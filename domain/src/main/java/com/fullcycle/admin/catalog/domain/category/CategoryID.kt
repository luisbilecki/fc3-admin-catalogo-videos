package com.fullcycle.admin.catalog.domain.category

import com.fullcycle.admin.catalog.domain.Identifier
import java.util.*

class CategoryID private constructor(override val value: String) : Identifier() {

    companion object {
        fun unique() = from(UUID.randomUUID())

        fun from(anId: String) = CategoryID(anId)

        fun from(anId: UUID) = CategoryID(anId.toString().lowercase(Locale.getDefault()))

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryID

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

}