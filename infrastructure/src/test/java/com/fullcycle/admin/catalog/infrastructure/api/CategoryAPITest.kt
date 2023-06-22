package com.fullcycle.admin.catalog.infrastructure.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fullcycle.admin.catalog.ControllerTest
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryOutput
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase
import com.fullcycle.admin.catalog.domain.category.CategoryID
import com.fullcycle.admin.catalog.infrastructure.category.models.CreateCategoryApiInput
import io.vavr.API.Right
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*


@ControllerTest(controllers = [CategoryAPI::class])
class CategoryAPITest @Autowired constructor(
    private val mvc: MockMvc,
    @MockBean val createCategoryUseCase: CreateCategoryUseCase,
    private val mapper: ObjectMapper
) {

    @Test
    @Throws(Exception::class)
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
}