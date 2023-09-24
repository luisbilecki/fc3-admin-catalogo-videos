package com.fullcycle.admin.catalog

import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.function.Consumer


class MySQLCleanUpExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val appContext = SpringExtension.getApplicationContext(context)
        cleanUp(
            listOf<CrudRepository<*, *>>(
                appContext.getBean(GenreRepository::class.java),
                appContext.getBean(CategoryRepository::class.java)
            )
        )

        val em = appContext.getBean(TestEntityManager::class.java)
        em.flush()
        em.clear()
    }

    private fun cleanUp(repositories: Collection<CrudRepository<*, *>>) {
        repositories.forEach(Consumer { obj: CrudRepository<*, *> -> obj.deleteAll() })
    }
}