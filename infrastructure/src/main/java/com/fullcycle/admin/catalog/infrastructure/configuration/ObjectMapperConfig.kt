package com.fullcycle.admin.catalog.infrastructure.configuration

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalog.infrastructure.configuration.json.Json
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
open class ObjectMapperConfig {

    @Bean
    open fun objectMapper(): ObjectMapper = Json.mapper()
}