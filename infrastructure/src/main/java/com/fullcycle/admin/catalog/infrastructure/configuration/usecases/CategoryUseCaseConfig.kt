package com.fullcycle.admin.catalog.infrastructure.configuration.usecases

import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalog.application.category.create.DefaultCreateCategoryUseCase
import com.fullcycle.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase
import com.fullcycle.admin.catalog.application.category.delete.DeleteCategoryUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase
import com.fullcycle.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase
import com.fullcycle.admin.catalog.application.category.update.UpdateCategoryUseCase
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
open class CategoryUseCaseConfig(private val categoryGateway: CategoryGateway) {

    @Bean
    open fun createCategoryUseCase(): CreateCategoryUseCase = DefaultCreateCategoryUseCase(categoryGateway)

    @Bean
    open fun updateCategoryUseCase(): UpdateCategoryUseCase = DefaultUpdateCategoryUseCase(categoryGateway)

    @Bean
    open fun getCategoryByIdUseCase(): GetCategoryByIdUseCase = DefaultGetCategoryByIdUseCase(categoryGateway)

    @Bean
    open fun listCategoriesUseCase(): ListCategoriesUseCase = DefaultListCategoriesUseCase(categoryGateway)

    @Bean
    open fun deleteCategoryUseCase(): DeleteCategoryUseCase = DefaultDeleteCategoryUseCase(categoryGateway)
}