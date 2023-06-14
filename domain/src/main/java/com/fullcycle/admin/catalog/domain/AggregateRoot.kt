package com.fullcycle.admin.catalog.domain

open class AggregateRoot<ID: Identifier> : Entity<ID>  {
    constructor(id: ID) : super(id)
}