package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class CategoryResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("is_active") val active: Boolean,
    @JsonProperty("created_at") val createdAt: Instant,
    @JsonProperty("updated_at") val updatedAt: Instant,
    @JsonProperty("deleted_at") val deletedAt: Instant?
)