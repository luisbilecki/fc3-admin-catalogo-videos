package com.fullcycle.admin.catalog.infrastructure.utils

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


object SpecificationUtils {
    fun <T> like(prop: String, term: String): Specification<T> {
        return Specification { root: Root<T>, _: CriteriaQuery<*>, cb: CriteriaBuilder ->
            cb.like(
                cb.upper(root.get(prop)), like(term.uppercase(Locale.getDefault()))
            )
        }
    }

    private fun like(term: String) = "%$term%"
}