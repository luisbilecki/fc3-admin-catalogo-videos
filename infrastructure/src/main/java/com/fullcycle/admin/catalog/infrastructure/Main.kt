package com.fullcycle.admin.catalog.infrastructure

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalog.infrastructure.configuration.WebServerConfig
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.AbstractEnvironment


@SpringBootApplication
open class Main

fun main(args: Array<String>) {
    System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development")
    SpringApplication.run(WebServerConfig::class.java, *args)
}

@Bean
fun runner(repository: CategoryRepository): ApplicationRunner? {
    return ApplicationRunner {
        val filmes = Category.newCategory("Filmes", null, true)
        repository.saveAndFlush(CategoryJpaEntity.from(filmes))
        repository.deleteAll()
    }
}