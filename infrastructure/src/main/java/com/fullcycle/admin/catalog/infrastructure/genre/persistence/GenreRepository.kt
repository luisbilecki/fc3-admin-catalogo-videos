package com.fullcycle.admin.catalog.infrastructure.genre.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository : JpaRepository<GenreJpaEntity, String>