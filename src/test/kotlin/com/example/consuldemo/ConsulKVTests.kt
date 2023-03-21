package com.example.consuldemo

import com.ecwid.consul.v1.ConsulClient
import com.example.consuldemo.client.ClientApp
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.testcontainers.shaded.org.bouncycastle.crypto.tls.ConnectionEnd.client

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = [ClientApp::class]
)
@TestPropertySource(
        properties = [
            "spring.cloud.consul.config.enabled=false",
            "spring.cloud.consul.discovery.enabled=false",
            "spring.cloud.discovery.enabled=false",
            "server.port=0", "management.endpoints.enabled-by-default=false",
            "spring.cloud.service-registry.auto-registration.enabled=false",

            ]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConsulKVTests : ConsulContainerSetup() {

    @Value("\${spring.cloud.consul.port}")
    lateinit var port: String


    @Test
    fun contextLoads() {
    }

    @Test
    fun getKeys() {
        val client = ConsulClient("localhost", port.toInt())
        client.setKVValue("/test/config", "TEST")
        val keys = client.getKVKeysOnly("")

        Assertions
                .assertThat(keys.value.size)
                .isGreaterThan(0)
    }
}