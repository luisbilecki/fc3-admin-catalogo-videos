package com.fullcycle.admin.catalog.infrastructure.genre.persistence

import com.fullcycle.admin.catalog.domain.category.CategoryID
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "genres_categories")
class GenreCategoryJpaEntity {
    @EmbeddedId
    var id: GenreCategoryID = null

    @ManyToOne
    @MapsId("genreId")
    val genre: GenreJpaEntity = null

    constructor()
    private constructor(genre: GenreJpaEntity, categoryId: CategoryID) {
        id = GenreCategoryID.from(genre.getId(), categoryId.value)
        this.genre = genre
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GenreCategoryJpaEntity
        return id == that.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    fun setId(id: GenreCategoryID?): GenreCategoryJpaEntity {
        this.id = id
        return this
    }

    fun setGenre(genre: GenreJpaEntity?): GenreCategoryJpaEntity {
        this.genre = genre
        return this
    }

    companion object {
        fun from(aGenre: GenreJpaEntity, aCategoryId: CategoryID): GenreCategoryJpaEntity {
            return GenreCategoryJpaEntity(aGenre, aCategoryId)
        }
    }
}