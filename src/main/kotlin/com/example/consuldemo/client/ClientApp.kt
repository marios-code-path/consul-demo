package com.example.consuldemo.client

import com.ecwid.consul.v1.ConsulClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.rsocket.transport.netty.client.TcpClientTransport
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.context.support.GenericApplicationContext
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.util.*
import java.util.concurrent.CountDownLatch
import kotlin.system.exitProcess


@SpringBootApplication
@EnableDiscoveryClient
@Profile("client")
class ClientApp {
    val latch: CountDownLatch = CountDownLatch(3)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ClientApp>(*args)
        }
    }

    @Bean
    fun listenOnStart(client: ConsulClient, mapper: ObjectMapper): ApplicationListener<ApplicationStartedEvent> = ApplicationListener {
        val getVal = client.getKVValue("/app/myproperties").value
        val kVal = Base64.getDecoder().decode(getVal.value.toString())
        val props = mapper.readValue(kVal, Map::class.java)

        props.keys.forEach { k ->
            println("$k: ${props[k]}")
        }
    }
        @Bean
        @LoadBalanced
        fun webClientBuilder(): WebClient.Builder {
            return WebClient.builder()
        }

        @Bean
        fun loadBalancedRequester(lbClient: LoadBalancerClient,
                                  dc: DiscoveryClient,
                                  builder: RSocketRequester.Builder): RSocketRequester {
            val ch = lbClient.choose("test")
            val port = ch.metadata["rsocketPort"]!!.toInt()

            return builder.transport(TcpClientTransport.create(ch.host, port))
        }

        @Bean
        fun makeCalls(requester: RSocketRequester, builder: WebClient.Builder) = CommandLineRunner {
            requester.route("prop")
                    .retrieveMono(String::class.java)
                    .doFinally { latch.countDown() }
                    .subscribe { println("GET PROP $it") }

            requester.route("app")
                    .retrieveMono(String::class.java)
                    .doFinally { latch.countDown() }
                    .subscribe { println("GET APP $it") }

            builder
                    .build().get().uri("http://test/actuator/health")
                    .retrieve()
                    .bodyToMono<String>()
                    .doFinally { latch.countDown() }
                    .subscribe { println("Status is: $it") }
        }

        @Bean
        fun shutdownWait(context: GenericApplicationContext) = CommandLineRunner {
            latch.await()
            println("CLOSING")

            context.close()
            exitProcess(0)
        }

    }