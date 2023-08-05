package com.fullcycle.admin.catalog.application.genre.retrieve.get

import com.fullcycle.admin.catalog.application.UseCase

abstract class GetGenreByIdUseCase : UseCase<String, GenreOutput>()