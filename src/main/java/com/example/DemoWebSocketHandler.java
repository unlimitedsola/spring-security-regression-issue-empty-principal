package com.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Rob Winch
 */
public class DemoWebSocketHandler implements WebSocketHandler {
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		Flux<WebSocketMessage> message = session.getHandshakeInfo()
			.getPrincipal()
			.map(it -> {
				return session.textMessage(it.getName());
			})
			.switchIfEmpty(Mono.fromSupplier(() -> session.textMessage("ERROR")))
			.concatWith(session.receive().then(Mono.empty()));

		return session.send(message);
	}
}
