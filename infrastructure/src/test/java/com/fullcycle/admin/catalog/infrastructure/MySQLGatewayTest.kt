package com.fullcycle.admin.catalog.infrastructure

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.annotation.Inherited
import java.util.function.Consumer


@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ComponentScan(includeFilters = [ComponentScan.Filter(type = FilterType.REGEX, pattern = arrayOf(".*[MySQLGateway]"))])
@DataJpaTest
@ExtendWith(
    MySQLGatewayTest.CleanUpExtensions::class
)
annotation class MySQLGatewayTest {
    class CleanUpExtensions : BeforeEachCallback {
        override fun beforeEach(context: ExtensionContext) {
            val repositories: Collection<CrudRepository<*, *>> =
                SpringExtension.getApplicationContext(context).getBeansOfType(CrudRepository::class.java).values
            cleanUp(repositories)
        }

        private fun cleanUp(repositories: Collection<CrudRepository<*, *>>) {
            repositories.forEach(Consumer { obj: CrudRepository<*, *> -> obj.deleteAll() })
        }
    }
}