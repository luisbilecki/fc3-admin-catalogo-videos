package com.fullcycle.admin.catalog.domain.genre

import com.fullcycle.admin.catalog.domain.Identifier
import java.util.*

class GenreID private constructor(override val value: String) : Identifier() {

    init {
        Objects.requireNonNull(value)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GenreID
        return value == that.value
    }

    override fun hashCode(): Int {
        return Objects.hash(value)
    }

    companion object {

        fun unique(): GenreID {
            return from(UUID.randomUUID())
        }

        fun from(id: String): GenreID {
            return GenreID(id)
        }

        fun from(id: UUID): GenreID {
            return GenreID(id.toString().lowercase(Locale.getDefault()))
        }
    }
}