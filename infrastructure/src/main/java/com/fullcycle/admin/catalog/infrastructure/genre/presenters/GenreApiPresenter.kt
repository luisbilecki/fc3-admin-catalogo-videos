package com.fullcycle.admin.catalog.infrastructure.genre.presenters

import com.fullcycle.admin.catalog.application.genre.retrieve.get.GenreOutput
import com.fullcycle.admin.catalog.application.genre.retrieve.list.GenreListOutput
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreListResponse
import com.fullcycle.admin.catalog.infrastructure.genre.models.GenreResponse


interface GenreApiPresenter {
    companion object {
        fun present(output: GenreOutput): GenreResponse? {
            return GenreResponse(
                output.id,
                output.name,
                output.categories,
                output.isActive,
                output.createdAt,
                output.updatedAt,
                output.deletedAt
            )
        }

        fun present(output: GenreListOutput): GenreListResponse? {
            return GenreListResponse(
                output.id,
                output.name,
                output.isActive,
                output.createdAt,
                output.deletedAt
            )
        }
    }
}