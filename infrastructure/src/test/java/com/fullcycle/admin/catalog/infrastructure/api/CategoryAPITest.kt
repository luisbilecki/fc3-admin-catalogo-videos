package com.fullcycle.admin.catalog.infrastructure.api

import com.fullcycle.admin.catalog.ControllerTest
import com.fullcycle.admin.catalog.application.category.create.CreateCategoryUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc

@ControllerTest(controllers = [CategoryAPI::class])
class CategoryAPITest @Autowired constructor(
    val mvc: MockMvc,
    @MockBean val createCategoryUseCase: CreateCategoryUseCase
) {

    @Test
    fun test() {
    }
}