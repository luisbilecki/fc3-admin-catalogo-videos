package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant


data class CategoryListResponse(
    @get:JsonProperty("id") val id: String,
    @get:JsonProperty("name") val name: String?,
    @get:JsonProperty("description") val description: String?,
    @get:JsonProperty("is_active") val isActive: Boolean,
    @get:JsonProperty("created_at") val createdAt: Instant,
    @get:JsonProperty("deleted_at") val deletedAt: Instant?
)