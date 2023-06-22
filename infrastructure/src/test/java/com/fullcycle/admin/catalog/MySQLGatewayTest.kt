package com.fullcycle.admin.catalog

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.FilterType
import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.Inherited


@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ComponentScan(includeFilters = [Filter(type = FilterType.REGEX, pattern = arrayOf(".[MySQLGateway]"))])
@DataJpaTest
@ExtendWith(CleanUpExtension::class)
annotation class MySQLGatewayTest