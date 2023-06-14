package com.fullcycle.admin.catalog.domain.exceptions

open class NoStackTraceException @JvmOverloads constructor(message: String?, cause: Throwable? = null) : RuntimeException(message, cause, true, false)