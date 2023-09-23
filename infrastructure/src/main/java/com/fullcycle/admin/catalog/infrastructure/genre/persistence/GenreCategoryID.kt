package com.fullcycle.admin.catalog.infrastructure.genre.persistence

import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable


@Embeddable
class GenreCategoryID {
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

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GenreCategoryID
        return genreId == that.genreId && categoryId == that.categoryId
    }

    override fun hashCode(): Int {
        return Objects.hash(genreId, categoryId)
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
        fun from(aGenreId: String, aCategoryId: String): GenreCategoryID {
            return GenreCategoryID(aGenreId, aCategoryId)
        }
    }
}