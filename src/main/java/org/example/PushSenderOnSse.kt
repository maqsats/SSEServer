package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.delay
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    // Start Ktor server
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            anyHost()
        }

        println("Server started at http://localhost:8080")

        routing {
            get("/sse") {
                call.response.cacheControl(CacheControl.NoCache(null))
                call.respondTextWriter(contentType = ContentType.Text.EventStream) {
                    while (true) {
                        val title = scanner.nextLine()
                        val body = scanner.nextLine()

                        // Send the notification as SSE message
                        val message = "data: {\"title\": \"$title\", \"body\": \"$body\"}\n\n"
                        println(message)
                        write(message)
                        flush()

                        // Simulate a delay before accepting the next notification
                        delay(2000)
                    }
                }
            }
        }
    }.start(wait = true)
}