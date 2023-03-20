package com.example.consuldemo.server

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Profile

@ConfigurationProperties("app")
@RefreshScope
@Profile("server")
open class AppProperties {
    var properties: Map<String, String> = mutableMapOf()
        get() = field
}