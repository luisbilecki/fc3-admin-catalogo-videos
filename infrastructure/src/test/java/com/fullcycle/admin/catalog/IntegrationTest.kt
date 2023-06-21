package com.fullcycle.admin.catalog

import com.fullcycle.admin.catalog.infrastructure.configuration.WebServerConfig
import com.fullcycle.admin.catalog.infrastructure.configuration.usecases.CategoryUseCaseConfig
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = [WebServerConfig::class])
@ExtendWith(CleanUpExtension::class)
annotation class IntegrationTest