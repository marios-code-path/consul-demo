package com.example.consuldemo.server

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("server")
class DemoService(private val container: AppStateContainer) {
    fun getPropertyProp(): String {
        return (container.prop) ?: ("null" + ",")
    }

    fun getPropertyApp(): String {
        val newAppProps = container.app.properties

        return newAppProps.keys.joinToString { "$it = ${newAppProps[it]}" }
    }
}