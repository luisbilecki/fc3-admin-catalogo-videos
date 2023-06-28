package com.fullcycle.admin.catalog.domain
abstract class Identifier : ValueObject() {
    abstract val value: String?
}