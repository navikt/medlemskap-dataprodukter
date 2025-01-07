package no.nav.medlemskap.dataprodukter.services

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }
private val secureLogger = KotlinLogging.logger("tjenestekall")

fun Routing.naisRoutes(

) {
    get("/isAlive") {

            call.respondText("Alive!", ContentType.Text.Plain, HttpStatusCode.OK)

    }
    get("/isReady") {
        call.respondText("Ready!", ContentType.Text.Plain, HttpStatusCode.OK)
    }

    }
