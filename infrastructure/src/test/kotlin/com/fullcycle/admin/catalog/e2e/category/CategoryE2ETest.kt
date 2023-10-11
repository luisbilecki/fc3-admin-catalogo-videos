package com.fullcycle.admin.catalog.e2e.category

import com.fullcycle.admin.catalog.E2ETest
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.infrastructure.category.models.CategoryResponse
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryRequest
import com.fullcycle.admin.catalog.infrastructure.category.models.UpdateCategoryRequest
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@E2ETest
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

    @Test
    fun asACatalogAdminIShouldBeAbleToNavigateToAllCategories() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        givenACategory("Filmes", "", true)
        givenACategory("Documentários", "", true)
        givenACategory("Séries", "", true)

        listCategories(0, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")))

        listCategories(1, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(1)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")))

        listCategories(2, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(2)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Séries")))

        listCategories(3, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(3)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize<Int>(0)))
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        givenACategory("Filmes", "", true)
        givenACategory("Documentários", "", true)
        givenACategory("Séries", "", true)

        listCategories(0, 1, "fil")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(1)))
            .andExpect(jsonPath("$.items", hasSize<Int>(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")))
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        givenACategory("Filmes", "C", true)
        givenACategory("Documentários", "Z", true)
        givenACategory("Séries", "A", true)

        listCategories(0, 3, "", "description", "desc")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(3)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize<Int>(3)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")))
            .andExpect(jsonPath("$.items[1].name", equalTo("Filmes")))
            .andExpect(jsonPath("$.items[2].name", equalTo("Séries")))
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() {
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

    @Test
    fun asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        val request = get("/categories/123")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)

        mvc.perform(request)
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")))
    }

    @Test
    @Throws(Exception::class)
    fun asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        val actualId = givenACategory("Movies", "", true)
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val requestBody = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)
        val request = put("/categories/" + actualId.value)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody))

        mvc.perform(request)
            .andExpect(status().isOk())

        val actualCategory = categoryRepository.findById(actualId.value).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNull(actualCategory.deletedAt)
    }

    @Test
    @Throws(Exception::class)
    fun asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = false
        val actualId = givenACategory(expectedName, expectedDescription, true)
        val requestBody = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)
        val request = put("/categories/" + actualId.value)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody))

        mvc.perform(request)
            .andExpect(status().isOk())

        val actualCategory = categoryRepository.findById(actualId.value).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNotNull(actualCategory.deletedAt)
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val actualId = givenACategory(expectedName, expectedDescription, false)
        val requestBody = UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive)
        val request = put("/categories/" + actualId.value)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody))

        mvc.perform(request)
            .andExpect(status().isOk())

        val actualCategory = categoryRepository.findById(actualId.value).get()

        Assertions.assertEquals(expectedName, actualCategory.name)
        Assertions.assertEquals(expectedDescription, actualCategory.description)
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive)
        Assertions.assertNotNull(actualCategory.createdAt)
        Assertions.assertNotNull(actualCategory.updatedAt)
        Assertions.assertNull(actualCategory.deletedAt)
    }

    @Test
    fun asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
        Assertions.assertEquals(0, categoryRepository.count())

        val actualId = givenACategory("Filmes", "", true)

        mvc.perform(
            delete("/categories/" + actualId.value)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNoContent())

        Assertions.assertFalse(categoryRepository.existsById(actualId.value))
    }

    private fun listCategories(page: Int, perPage: Int): ResultActions {
        return listCategories(page, perPage, "", "", "")
    }

    private fun listCategories(page: Int, perPage: Int, search: String): ResultActions {
        return listCategories(page, perPage, search, "", "")
    }

    private fun listCategories(
        page: Int,
        perPage: Int,
        search: String,
        sort: String,
        direction: String
    ): ResultActions {
        val request = get("/categories")
            .queryParam("page", page.toString())
            .queryParam("perPage", perPage.toString())
            .queryParam("search", search)
            .queryParam("sort", sort)
            .queryParam("dir", direction)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)

        return mvc.perform(request)
    }
    private fun givenACategory(name: String, description: String, isActive: Boolean): CategoryID {
        val requestBody = CreateCategoryRequest(name, description, isActive)
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

        @JvmStatic
        @Container
        private val MYSQL_CONTAINER = MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos")

        @JvmStatic
        @DynamicPropertySource
        fun setDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("mysql.port") { MYSQL_CONTAINER.getMappedPort(3306) }
        }
    }
}