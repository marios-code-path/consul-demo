package com.example.consuldemo.server

import com.ecwid.consul.v1.ConsulClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
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

    @Bean
    fun startedListener(client: ConsulClient, mapper: ObjectMapper) = ApplicationListener<ApplicationStartedEvent> {
        println("Application started")
        val properties = mapOf("foo" to "bar", "baz" to "qux", "quux" to "quuz")
        client.setKVValue("/app/myproperties", mapper.writeValueAsString(properties))
    }

}
