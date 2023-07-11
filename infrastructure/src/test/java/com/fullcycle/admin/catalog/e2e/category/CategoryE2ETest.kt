package com.fullcycle.admin.catalog.e2e.category

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class CategoryE2ETest {

    @Test
    fun testWorks() {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning)
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