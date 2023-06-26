package com.fullcycle.admin.catalog.infrastructure.category.models

import com.fullcycle.admin.catalog.JacksonTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.json.JacksonTester


@JacksonTest
class CategoryResponseTest @Autowired constructor(val json: JacksonTester<CategoryResponse>) {
}