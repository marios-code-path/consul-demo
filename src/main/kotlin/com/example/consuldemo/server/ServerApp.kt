package com.example.consuldemo.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
@Profile("server")
class ServerApp {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ServerApp>(*args)
        }
    }

    @Bean
    fun propertySourcesPlaceholderConfigurer(): PropertySourcesPlaceholderConfigurer? {
        val configurer = PropertySourcesPlaceholderConfigurer()
        configurer.setIgnoreUnresolvablePlaceholders(true)
        return configurer
    }

}
