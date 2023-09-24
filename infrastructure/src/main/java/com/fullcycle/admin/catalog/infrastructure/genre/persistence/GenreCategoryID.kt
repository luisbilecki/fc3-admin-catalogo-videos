package com.fullcycle.admin.catalog.infrastructure.genre.persistence

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class GenreCategoryID : Serializable {
    @Column(name = "genre_id", nullable = false)
    var genreId: String? = null
        private set

    @Column(name = "category_id", nullable = false)
    var categoryId: String? = null
        private set

    constructor()
    private constructor(aGenreId: String, aCategoryId: String) {
        genreId = aGenreId
        categoryId = aCategoryId
    }

    fun setGenreId(genreId: String?): GenreCategoryID {
        this.genreId = genreId
        return this
    }

    fun setCategoryId(categoryId: String?): GenreCategoryID {
        this.categoryId = categoryId
        return this
    }

    companion object {
        fun from(genreId: String, categoryId: String) = GenreCategoryID(genreId, categoryId)
    }
}