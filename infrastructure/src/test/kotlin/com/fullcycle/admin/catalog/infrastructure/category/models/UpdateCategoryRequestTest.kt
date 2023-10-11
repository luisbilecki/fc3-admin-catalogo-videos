package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fullcycle.admin.catalog.JacksonTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester

@JacksonTest
class UpdateCategoryRequestTest @Autowired constructor(private val json: JacksonTester<UpdateCategoryRequest>) {

    @Test
    fun testUnmarshall() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val json = """
        {
          "name": "%s",
          "description": "%s",
          "is_active": %s
        }    
        
        """.trimIndent().formatted(expectedName, expectedDescription, expectedIsActive)

        val actualJson = this.json.parse(json)

        Assertions.assertThat(actualJson)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedIsActive)
    }
}