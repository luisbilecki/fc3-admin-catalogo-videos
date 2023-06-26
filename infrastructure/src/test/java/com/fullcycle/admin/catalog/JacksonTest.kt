package com.fullcycle.admin.catalog

import com.fullcycle.admin.catalog.infrastructure.configuration.ObjectMapperConfig
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited


@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test")
@JsonTest(
    includeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = arrayOf(ObjectMapperConfig::class)
    )]
)
annotation class JacksonTest