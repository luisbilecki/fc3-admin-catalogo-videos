package com.fullcycle.admin.catalog.infrastructure.configuration.usecases

import com.fullcycle.admin.catalog.application.category.create.DefaultCreateCategoryUseCase
import com.fullcycle.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase
import com.fullcycle.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class CategoryUseCaseConfig(private val categoryGateway: CategoryGateway) {

    @Bean
    open fun createCategoryUseCase() = DefaultCreateCategoryUseCase(categoryGateway)

    @Bean
    open fun updateCategoryUseCase() = DefaultUpdateCategoryUseCase(categoryGateway)

    @Bean
    open fun getCategoryByIdUseCase() = DefaultGetCategoryByIdUseCase(categoryGateway)

    @Bean
    open fun listCategoriesUseCase() = DefaultListCategoriesUseCase(categoryGateway)

    @Bean
    open fun deleteCategoryUseCase() = DefaultDeleteCategoryUseCase(categoryGateway)
}