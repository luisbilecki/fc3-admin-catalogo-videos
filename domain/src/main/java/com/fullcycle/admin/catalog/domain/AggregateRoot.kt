package com.fullcycle.admin.catalog.domain

class AggregateRoot<ID: Identifier> : Entity<ID>  {
    constructor(id: ID) : super(id)
}