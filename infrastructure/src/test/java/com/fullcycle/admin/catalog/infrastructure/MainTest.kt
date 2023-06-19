package com.fullcycle.admin.catalog.infrastructure

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.runApplication
import org.springframework.core.env.AbstractEnvironment

class MainTest {

    @Test
    fun testMain() {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test")
        Assertions.assertNotNull(Main())
        runApplication<Main>(*emptyArray<String>())
    }
}