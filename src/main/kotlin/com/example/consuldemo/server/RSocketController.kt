package com.example.consuldemo.server

import org.springframework.context.annotation.Profile
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
@Profile("server")
class RSocketController(val that: DemoService) {

    @MessageMapping("prop")
    fun prop(): String = that.getPropertyProp()

    @MessageMapping("app")
    fun app(): String = that.getPropertyApp()
}