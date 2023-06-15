package com.fullcycle.admin.catalog.application

abstract class NullaryUseCase<OUT> {
    abstract fun execute(): OUT
}