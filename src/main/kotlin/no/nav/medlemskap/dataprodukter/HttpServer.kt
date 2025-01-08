package no.nav.medlemskap.dataprodukter

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import no.nav.medlemskap.dataprodukter.services.naisRoutes
import org.slf4j.event.Level
import java.util.*

const val MDC_CALL_ID = "Nav-Call-Id"
val objectMapper = ObjectMapper()
fun createHttpServer() = embeddedServer(Netty, port = 8080) {



        install(CallId) {
            header(MDC_CALL_ID)
            generate { UUID.randomUUID().toString() }
        }

        install(CallLogging) {
            level = Level.INFO
            callIdMdc(MDC_CALL_ID)
        }


        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(objectMapper))
        }


        routing {
            naisRoutes()
            }
        }







