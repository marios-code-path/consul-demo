package com.example.consuldemo.server

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("server")
class RESTController(val service: DemoService) {

    @GetMapping("/prop")
    fun prop(): String = service.getPropertyProp()

    @GetMapping("/app")
    fun test(): String = service.getPropertyApp()
}