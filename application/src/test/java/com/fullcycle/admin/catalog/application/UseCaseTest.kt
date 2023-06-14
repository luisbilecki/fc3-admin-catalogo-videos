package com.fullcycle.admin.catalog.application

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UseCaseTest {
    @Test
    fun testCreateUseCase() {
        Assertions.assertNotNull(UseCase())
        Assertions.assertNotNull(UseCase().execute())
    }
}
