package com.example

import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

/**
 * @author Sola
 */
object DemoWebSocketHandler : WebSocketHandler {

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.handshakeInfo.principal.map {
            session.textMessage(it.toString())
        }.switchIfEmpty(Mono.just(session.textMessage("You shouldn't be seeing this.")))
            .concatWith(session.receive().then(Mono.empty()))
            .let(session::send)
    }

}
