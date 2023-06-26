package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant


data class CategoryListResponse(
    @JsonProperty("id") val id: String,
    @JsonProperty("name") val name: String?,
    @JsonProperty("description") val description: String?,
    @JsonProperty("is_active") val isActive: Boolean,
    @JsonProperty("created_at") val createdAt: Instant,
    @JsonProperty("deleted_at") val deletedAt: Instant?
)