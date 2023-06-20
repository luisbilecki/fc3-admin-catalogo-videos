package com.fullcycle.admin.catalog.infrastructure

import com.fullcycle.admin.catalog.infrastructure.configuration.WebServerConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.AbstractEnvironment


@SpringBootApplication
open class Main

fun main(args: Array<String>) {
    System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development")
    SpringApplication.run(WebServerConfig::class.java, *args)
}
