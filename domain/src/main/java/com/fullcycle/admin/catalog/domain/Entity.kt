package com.fullcycle.admin.catalog.domain

import java.util.*

abstract class Entity<ID : Identifier>(id: ID) {

    init {
        Objects.requireNonNull(id, "'id' should not be null")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}