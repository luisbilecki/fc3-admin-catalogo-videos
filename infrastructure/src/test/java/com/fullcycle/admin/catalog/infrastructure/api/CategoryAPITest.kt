package com.fullcycle.admin.catalog.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fullcycle.admin.catalog.ControllerTest
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryOutput
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalog.application.category.retrieve.get.CategoryOutput
import com.fullcycle.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase
import com.fullcycle.admin.catalog.domain.category.Category
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.domain.exceptions.DomainException
import com.fullcycle.admin.catalog.domain.validation.Error
import com.fullcycle.admin.catalog.domain.validation.handler.Notification
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryApiInput
import io.vavr.API.Left
import io.vavr.API.Right
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*


@ControllerTest(controllers = [CategoryAPI::class])
class CategoryAPITest @Autowired constructor(
    private val mvc: MockMvc,
    @MockBean val createCategoryUseCase: CreateCategoryUseCase,
    @MockBean val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val mapper: ObjectMapper
) {

    @Test
    fun givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true

        val input = CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive)

        `when`(createCategoryUseCase.execute(any()))
            .thenReturn(Right(CreateCategoryOutput.from("123")))

        val request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(input))

        mvc.perform(request)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/categories/123"))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat { cmd ->
            (Objects.equals(expectedName, cmd.name)
                    && Objects.equals(expectedDescription, cmd.description)
                    && Objects.equals(expectedIsActive, cmd.isActive))
        })
    }

    @Test
    fun givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() {
        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be null"

        val input = CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive)

        `when`(createCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(Error(expectedMessage))))

        val request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(input))

        mvc.perform(request)
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize<Error>(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)))

        verify(createCategoryUseCase, times(1)).execute(argThat { cmd ->
            expectedName == cmd.name && expectedDescription == cmd.description && expectedIsActive == cmd.isActive
        })
    }

    @Test
    fun givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() {
        val expectedName: String? = null
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val expectedMessage = "'name' should not be null"
        val input = CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive)

        `when`(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(Error(expectedMessage)))

        val request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(input))

        mvc.perform(request)
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedMessage)))
            .andExpect(jsonPath("$.errors", hasSize<Error>(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)))

        verify(createCategoryUseCase, times(1)).execute(argThat { cmd ->
            expectedName == cmd.name && expectedDescription == cmd.description && expectedIsActive == cmd.isActive
        })
    }

    @Test
    fun givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        val expectedName = "Filmes"
        val expectedDescription = "A categoria mais assistida"
        val expectedIsActive = true
        val category = Category.newCategory(expectedName, expectedDescription, expectedIsActive)
        val expectedId = category.id.value

        `when`(getCategoryByIdUseCase.execute(any()))
            .thenReturn(CategoryOutput.from(category))

        val request = get("/categories/{id}", expectedId)
        val response = mvc.perform(request)
            .andDo(print())

        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.name", equalTo(expectedName)))
            .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
            .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
            .andExpect(jsonPath("$.created_at", equalTo(category.createdAt.toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(category.updatedAt.toString())))
            .andExpect(jsonPath("$.deleted_at", equalTo(category.deletedAt)))

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId))
    }

    @Test
    fun givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        val expectedErrorMessage = "Category with ID 123 was not found"
        val expectedId = CategoryID.from("123").value

        `when`(getCategoryByIdUseCase.execute(any()))
            .thenThrow(
                DomainException.with(
                    Error(String.format("Category with ID %s was not found", expectedId))
                )
            )

        val request = get("/categories/{id}", expectedId)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)

        val response = mvc.perform(request)
            .andDo(print())

        response.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
    }
}