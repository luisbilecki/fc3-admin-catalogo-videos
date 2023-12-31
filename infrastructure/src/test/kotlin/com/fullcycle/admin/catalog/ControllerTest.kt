package com.fullcycle.admin.catalog

import com.fullcycle.admin.catalog.infrastructure.configuration.ObjectMapperConfig
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

import org.springframework.core.annotation.AliasFor

import org.springframework.test.context.ActiveProfiles

import java.lang.annotation.Inherited
import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@WebMvcTest
@Import(ObjectMapperConfig::class)
annotation class ControllerTest(

    @get:AliasFor(
        annotation = WebMvcTest::class,
        attribute = "controllers"
    ) val controllers: Array<KClass<*>> = []
)