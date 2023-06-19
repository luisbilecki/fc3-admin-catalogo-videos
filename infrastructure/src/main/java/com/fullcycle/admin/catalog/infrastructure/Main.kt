package com.fullcycle.admin.catalog.infrastructure

import com.fullcycle.admin.catalog.infrastructure.configuration.WebServerConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class Main

fun main(args: Array<String>) {
    SpringApplication.run(WebServerConfig::class.java, *args)
}