package com.fullcycle.admin.catalog.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant


data class GenreResponse(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty(
        "name"
    ) @param:JsonProperty("name") val name: String?,
    @field:JsonProperty("categories_id") @param:JsonProperty("categories_id") val categories: List<String>,
    @field:JsonProperty(
        "is_active"
    ) @param:JsonProperty("is_active") val active: Boolean,
    @field:JsonProperty("created_at") @param:JsonProperty("created_at") val createdAt: Instant,
    @field:JsonProperty(
        "updated_at"
    ) @param:JsonProperty("updated_at") val updatedAt: Instant,
    @field:JsonProperty("deleted_at") @param:JsonProperty("deleted_at") val deletedAt: Instant?
)