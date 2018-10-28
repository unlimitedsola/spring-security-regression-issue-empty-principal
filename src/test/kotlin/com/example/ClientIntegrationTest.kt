package com.example

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import org.springframework.web.reactive.socket.client.WebSocketClient
import java.net.URI

/**
 * @author Sola
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    private val client: WebSocketClient = ReactorNettyWebSocketClient()

    @Test
    fun test() {
        client.execute(URI("ws://localhost:$port/test")) { session ->
            session.receive()
                .take(1)
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext {
                    assertThat(it).isNotEqualTo("You shouldn't be seeing this.")
                }.then()
        }.block()
    }

}
