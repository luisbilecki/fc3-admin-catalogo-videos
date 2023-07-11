package com.fullcycle.admin.catalog

import com.fullcycle.admin.catalog.infrastructure.configuration.WebServerConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = [WebServerConfig::class])
@ExtendWith(MySQLCleanUpExtension::class)
@AutoConfigureMockMvc
annotation class E2ETest