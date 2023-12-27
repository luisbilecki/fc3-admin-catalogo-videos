package com.fullcycle.admin.catalog.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant


data class GenreListResponse(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty(
        "name"
    ) @param:JsonProperty("name") val name: String?,
    @field:JsonProperty("is_active") @param:JsonProperty("is_active") val active: Boolean,
    @field:JsonProperty(
        "created_at"
    ) @param:JsonProperty("created_at") val createdAt: Instant,
    @field:JsonProperty("deleted_at") @param:JsonProperty("deleted_at") val deletedAt: Instant?
)