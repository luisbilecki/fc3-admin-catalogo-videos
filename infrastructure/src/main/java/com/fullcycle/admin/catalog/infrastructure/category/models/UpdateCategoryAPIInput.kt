package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateCategoryAPIInput(
    @JsonProperty("name") val name: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("is_active") val active: Boolean
)