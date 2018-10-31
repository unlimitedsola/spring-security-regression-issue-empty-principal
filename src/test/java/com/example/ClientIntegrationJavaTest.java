package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Rob Winch
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientIntegrationJavaTest {
	@LocalServerPort
	int port;

	WebSocketClient client = new ReactorNettyWebSocketClient();

	@Test
	public void client() throws Exception {
		this.client.execute(new URI("ws://localhost:"+ this.port +"/test"), s -> {
			return s.receive()
				.take(1)
				.map(WebSocketMessage::getPayloadAsText)
				.doOnNext(t -> assertThat(t).isEqualTo("the-username"))
				.then();
		})
		.block();
	}
}
