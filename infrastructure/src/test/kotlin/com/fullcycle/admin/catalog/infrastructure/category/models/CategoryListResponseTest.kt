package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fullcycle.admin.catalog.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant


@JacksonTest
class CategoryListResponseTest @Autowired constructor(private val json: JacksonTester<CategoryListResponse>) {

    @Test
    fun testMarshall() {
        val expectedId = "123"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()

        val response = CategoryListResponse(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive,
            expectedCreatedAt,
            expectedDeletedAt
        )

        val actualJson = json.write(response)

        Assertions.assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.name", expectedName)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
    }
}