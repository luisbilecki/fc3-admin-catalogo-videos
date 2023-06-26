package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fullcycle.admin.catalog.JacksonTest
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester
import java.time.Instant


@JacksonTest
class CategoryResponseTest @Autowired constructor(private val json: JacksonTester<CategoryResponse>) {

    @Test
    fun testMarshall() {
        val expectedId = "123"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()
        val response = CategoryResponse(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive,
            expectedCreatedAt,
            expectedUpdatedAt,
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
            .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
    }

    @Test
    fun testUnmarshall() {
        val expectedId = "123"
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val expectedCreatedAt = Instant.now()
        val expectedUpdatedAt = Instant.now()
        val expectedDeletedAt = Instant.now()
        val json = """
        {
          "id": "%s",
          "name": "%s",
          "description": "%s",
          "is_active": %s,
          "created_at": "%s",
          "deleted_at": "%s",
          "updated_at": "%s"
        }    
        
        """.trimIndent().formatted(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive,
            expectedCreatedAt.toString(),
            expectedDeletedAt.toString(),
            expectedUpdatedAt.toString()
        )
        val actualJson = this.json.parse(json)

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
            .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
            .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
            .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
    }
}