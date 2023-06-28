package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateCategoryRequest(
    @JsonProperty("name") val name: String?,
    @JsonProperty("description") val description: String,
    @param:JsonProperty("is_active") @get:JsonProperty("is_active") var active: Boolean
)