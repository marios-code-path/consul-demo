package com.example.consuldemo.server

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.endpoint.event.RefreshEvent
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@RefreshScope
@Profile("server")
class AppStateContainer(@Value("\${prop}") val prop: String, val app: AppProperties) {

    @EventListener(RefreshEvent::class)
    fun onRefreshEvent(event: RefreshEvent) {
        println("Refreshed App State")
    }
}