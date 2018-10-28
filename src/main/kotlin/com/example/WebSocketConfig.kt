package com.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import java.util.*

/**
 * @author Sola
 */
@Configuration
class WebSocketConfig {

    @Bean
    fun handlerMapping() = SimpleUrlHandlerMapping().apply {
        val map = HashMap<String, WebSocketHandler>()
        map["/test"] = DemoWebSocketHandler
        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = -1 // before annotated controllers
        return mapping
    }

    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter()

}
