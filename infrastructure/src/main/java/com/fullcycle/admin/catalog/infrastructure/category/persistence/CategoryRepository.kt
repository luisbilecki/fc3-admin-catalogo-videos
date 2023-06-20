package com.fullcycle.admin.catalog.infrastructure.category.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryJpaEntity, String>