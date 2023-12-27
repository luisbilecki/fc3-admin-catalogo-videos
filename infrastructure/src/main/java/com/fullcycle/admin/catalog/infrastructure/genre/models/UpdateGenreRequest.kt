package com.fullcycle.admin.catalog.infrastructure.genre.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateGenreRequest(
    @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
    @field:JsonProperty(
        "categories_id"
    ) @param:JsonProperty("categories_id") val categories: List<String>,
    @field:JsonProperty("is_active") @param:JsonProperty(
        "is_active"
    ) val active: Boolean
)