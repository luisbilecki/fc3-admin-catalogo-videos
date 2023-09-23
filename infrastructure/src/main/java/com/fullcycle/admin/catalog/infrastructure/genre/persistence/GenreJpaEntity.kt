package com.fullcycle.admin.catalog.infrastructure.genre.persistence

import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.genre.Genre
import com.fullcycle.admin.catalog.domain.genre.GenreID
import java.time.Instant
import java.util.function.Consumer
import javax.persistence.*


@Entity
@Table(name = "genres")
class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    var id: String? = null
        private set

    @Column(name = "name", nullable = false)
    var name: String? = null
        private set

    @Column(name = "active", nullable = false)
    var isActive = false
        private set

    @OneToMany(mappedBy = "genre", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    private var categories: MutableSet<GenreCategoryJpaEntity>? = null

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    var createdAt: Instant? = null
        private set

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    var updatedAt: Instant? = null
        private set

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    var deletedAt: Instant? = null
        private set

    constructor()
    private constructor(
        id: String,
        name: String?,
        isActive: Boolean,
        createdAt: Instant,
        updatedAt: Instant,
        deletedAt: Instant?
    ) {
        this.id = id
        this.name = name
        this.isActive = isActive
        categories = HashSet()
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.deletedAt = deletedAt
    }

    fun toAggregate(): Genre {
        return Genre.with(
            GenreID.from(id!!),
            name!!,
            isActive,
            getCategories()!!.stream()
                .map{ it: GenreCategoryJpaEntity ->
                    CategoryID.from(
                        it.id?.categoryId!!
                    )
                }
                .toList(),
            createdAt!!,
            updatedAt!!,
            deletedAt
        )
    }

    private fun addCategory(anId: CategoryID) {
        categories!!.add(GenreCategoryJpaEntity.from(this, anId))
    }

    private fun removeCategory(anId: CategoryID) {
        categories!!.remove(GenreCategoryJpaEntity.from(this, anId))
    }

    fun setId(id: String?): GenreJpaEntity {
        this.id = id
        return this
    }

    fun setName(name: String?): GenreJpaEntity {
        this.name = name
        return this
    }

    fun setActive(active: Boolean): GenreJpaEntity {
        isActive = active
        return this
    }

    fun getCategories(): Set<GenreCategoryJpaEntity>? {
        return categories
    }

    fun setCategories(categories: MutableSet<GenreCategoryJpaEntity>?): GenreJpaEntity {
        this.categories = categories
        return this
    }

    fun setCreatedAt(createdAt: Instant?): GenreJpaEntity {
        this.createdAt = createdAt
        return this
    }

    fun setUpdatedAt(updatedAt: Instant?): GenreJpaEntity {
        this.updatedAt = updatedAt
        return this
    }

    fun setDeletedAt(deletedAt: Instant?): GenreJpaEntity {
        this.deletedAt = deletedAt
        return this
    }

    companion object {
        fun from(aGenre: Genre): GenreJpaEntity {
            val anEntity = GenreJpaEntity(
                aGenre.id.value,
                aGenre.name,
                aGenre.isActive,
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
            )
            aGenre.categories
                .forEach(Consumer { anId: CategoryID -> anEntity.addCategory(anId) })
            return anEntity
        }
    }
}