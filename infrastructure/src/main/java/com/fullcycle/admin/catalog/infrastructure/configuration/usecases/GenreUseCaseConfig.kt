package com.fullcycle.admin.catalog.infrastructure.configuration.usecases

import com.fullcycle.admin.catalog.application.genre.create.CreateGenreUseCase
import com.fullcycle.admin.catalog.application.genre.create.DefaultCreateGenreUseCase
import com.fullcycle.admin.catalog.application.genre.delete.DefaultDeleteGenreUseCase
import com.fullcycle.admin.catalog.application.genre.delete.DeleteGenreUseCase
import com.fullcycle.admin.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase
import com.fullcycle.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase
import com.fullcycle.admin.catalog.application.genre.retrieve.list.DefaultListGenreUseCase
import com.fullcycle.admin.catalog.application.genre.retrieve.list.ListGenreUseCase
import com.fullcycle.admin.catalog.application.genre.update.DefaultUpdateGenreUseCase
import com.fullcycle.admin.catalog.application.genre.update.UpdateGenreUseCase
import com.fullcycle.admin.catalog.domain.category.CategoryGateway
import com.fullcycle.admin.catalog.domain.genre.GenreGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
open class GenreUseCaseConfig @Autowired constructor(
    private val categoryGateway: CategoryGateway,
    private val genreGateway: GenreGateway
) {

    @Bean
    open fun createGenreUseCase(): CreateGenreUseCase {
        return DefaultCreateGenreUseCase(categoryGateway, genreGateway)
    }

    @Bean
    open fun deleteGenreUseCase(): DeleteGenreUseCase {
        return DefaultDeleteGenreUseCase(genreGateway)
    }

    @get:Bean
    val genreByIdUseCase: GetGenreByIdUseCase
        get() = DefaultGetGenreByIdUseCase(genreGateway)

    @Bean
    open fun listGenreUseCase(): ListGenreUseCase {
        return DefaultListGenreUseCase(genreGateway)
    }

    @Bean
    open fun updateGenreUseCase(): UpdateGenreUseCase {
        return DefaultUpdateGenreUseCase(categoryGateway, genreGateway)
    }
}