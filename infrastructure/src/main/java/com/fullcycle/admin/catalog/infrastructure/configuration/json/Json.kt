package com.fullcycle.admin.catalog.infrastructure.configuration.json

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.afterburner.AfterburnerModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.util.concurrent.Callable

enum class Json {
    INSTANCE;

    private val mapper = Jackson2ObjectMapperBuilder()
        .dateFormat(StdDateFormat())
        .featuresToDisable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
            DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
        .modules(JavaTimeModule(), Jdk8Module(), afterburnerModule())
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .build<ObjectMapper>()

    private fun afterburnerModule(): AfterburnerModule {
        val module = AfterburnerModule()
        module.setUseValueClassLoader(false)
        return module
    }

    companion object {
        fun mapper(): ObjectMapper {
            return INSTANCE.mapper.copy()
        }

        fun writeValueAsString(obj: Any?): String {
            return invoke {
                INSTANCE.mapper.writeValueAsString(
                    obj
                )
            }
        }

        fun <T> readValue(json: String?, clazz: Class<T>?): T {
            return invoke { INSTANCE.mapper.readValue(json, clazz) }
        }

        private operator fun <T> invoke(callable: Callable<T>): T {
            return try {
                callable.call()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}