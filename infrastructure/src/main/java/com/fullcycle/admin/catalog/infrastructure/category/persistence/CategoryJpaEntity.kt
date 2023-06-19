package com.fullcycle.admin.catalog.infrastructure.category.persistence

import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "category")
class CategoryJpaEntity {

    @Id
    var id: String? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "description", length = 4000)
    var description: String? = null

    @Column(name = "active", nullable = false)
    var isActive = false

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    var createdAt: Instant? = null

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    var updatedAt: Instant? = null

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    var deletedAt: Instant? = null

    constructor()
    private constructor(
        id: String,
        name: String?,
        description: String?,
        active: Boolean,
        createdAt: Instant,
        updatedAt: Instant,
        deletedAt: Instant?
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.isActive = active
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.deletedAt = deletedAt
    }

    fun toAggregate() = Category.with(
        CategoryID.from(id!!),
        name,
        description,
        isActive,
        createdAt,
        updatedAt,
        deletedAt
    )

    companion object {
        fun from(category: Category) = CategoryJpaEntity(
            category.id.value,
            category.name,
            category.description,
            category.isActive,
            category.createdAt,
            category.updatedAt,
            category.deletedAt
        )
    }
}