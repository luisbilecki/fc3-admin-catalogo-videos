package com.fullcycle.admin.catalog.infrastructure

import com.fullcycle.admin.catalog.application.UseCase

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello world!")
        println(UseCase().execute())
    }
}