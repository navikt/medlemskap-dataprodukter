package no.nav.medlemskap.dataprodukter

import org.slf4j.Logger
import org.slf4j.LoggerFactory


fun main() {
    Application().start()
}

class Application() {
    companion object {
        val log: Logger = LoggerFactory.getLogger(Application::class.java)
    }

    fun start() {
        log.info("Start application")


        createHttpServer().start(wait = true)
    }
}