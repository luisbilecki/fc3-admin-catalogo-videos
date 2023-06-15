package com.fullcycle.admin.catalog.application


abstract class UseCase<IN, OUT> {
    abstract fun execute(input: IN): OUT
}