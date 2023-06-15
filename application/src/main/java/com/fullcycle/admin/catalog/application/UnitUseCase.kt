package com.fullcycle.admin.catalog.application

abstract class UnitUseCase<IN> {
    abstract fun execute(input: IN)
}