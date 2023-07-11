package com.fullcycle.admin.catalog.e2e.category

import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@Testcontainers
class CategoryE2ETest @Autowired constructor(
    private val mvc: MockMvc,
    private val categoryRepository: CategoryRepository
) {

    @Test
    fun asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val actualId = givenACategory(expectedName, expectedDescription, expectedIsActive)
        val actualCategory = retrieveACategory(actualId.value)

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNull(actualCategory.deletedAt)
    }

    private fun givenACategory(aName: String, aDescription: String, isActive: Boolean): CategoryID {
        val requestBody = CreateCategoryRequest(aName, aDescription, isActive)
        val request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody))
        val actualId = mvc.perform(request)
            .andExpect(status().isCreated())
            .andReturn()
            .response.getHeader("Location")
            .replace("/categories/", "")

        return CategoryID.from(actualId)
    }

    private fun retrieveACategory(anId: String): CategoryResponse {
        val request = get("/categories/$anId")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        val json = mvc.perform(request)
            .andExpect(status().isOk())
            .andReturn()
            .response.contentAsString

        return Json.readValue(json, CategoryResponse::class.java)
    }

    companion object {

        @Container
        private val MYSQL_CONTAINER = MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos")

        @DynamicPropertySource
        fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("mysql.port") {
                MYSQL_CONTAINER.getMappedPort(
                    3306
                )
            }
        }
    }
}