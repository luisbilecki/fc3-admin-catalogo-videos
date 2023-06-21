package com.fullcycle.admin.catalog

import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.function.Consumer

class CleanUpExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext) {
        val repositories: Collection<CrudRepository<*, *>> = SpringExtension
            .getApplicationContext(context)
            .getBeansOfType(CrudRepository::class.java)
            .values
        cleanUp(repositories)
    }

    private fun cleanUp(repositories: Collection<CrudRepository<*, *>>) {
        repositories.forEach(Consumer { obj: CrudRepository<*, *> -> obj.deleteAll() })
    }
}