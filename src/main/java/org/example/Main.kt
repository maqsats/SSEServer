package org.example

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            anyHost()
        }

        routing {
            get("/sse") {
                call.response.cacheControl(CacheControl.NoCache(null))
                call.respondTextWriter(contentType = ContentType.Text.EventStream) {
                    while (true) {
                        val message = "data: Hello at ${System.currentTimeMillis()}\n\n"
                        write(message)
                        flush()
                        delay(2000)
                    }
                }
            }
        }
    }.start(wait = true)
}
