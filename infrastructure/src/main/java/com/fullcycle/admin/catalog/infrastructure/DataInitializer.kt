package com.fullcycle.admin.catalog.infrastructure

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class DataInitializer(private val repository: CategoryRepository) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        val all = repository.findAll()
        val movies = Category.newCategory("Filmes", null, true)
        repository.saveAndFlush(CategoryJpaEntity.from(movies))
        repository.deleteAll()
    }
}