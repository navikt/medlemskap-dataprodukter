package no.nav.medlemskap.dataprodukter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import mu.KotlinLogging
import no.nav.medlemskap.dataprodukter.config.Environment
import no.nav.medlemskap.dataprodukter.config.KafkaConfig
import no.nav.medlemskap.dataprodukter.domain.VurdertMessageRecord

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class MedlemskapVurdertConsumer(
    environment: Environment,

    private val config: KafkaConfig = KafkaConfig(environment),
    private val consumer: KafkaConsumer<String, String> = config.createFlexConsumer(),

    ) {

    private val secureLogger = KotlinLogging.logger("tjenestekall")
    private val logger = KotlinLogging.logger { }

    init {
        consumer.subscribe(listOf(config.MedlemskapVurdertTopic))
    }

    fun pollMessages(): List<VurdertMessageRecord> =

        consumer.poll(Duration.ofSeconds(4))
            .map {
                VurdertMessageRecord(
                    partition = it.partition(),
                    offset = it.offset(),
                    value = it.value(),
                    key = it.key(),
                    topic = it.topic(),
                    timestamp = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(it!!.timestamp()), ZoneId.systemDefault()),
                    timestampType = it.timestampType().name
                )
            }



    fun flow(): Flow<List<VurdertMessageRecord>> =
        flow {
            while (true) {
                emit(emptyList<VurdertMessageRecord>()) //fjerne denne når vi er klare til å laste data i produksjon
                //emit(pollMessages()) // legg in denne når vi er klare til å laste data i produksjon
            }
        }.onEach { it ->
            logger.debug { "flex messages received :" + it.size + "on topic " + config.MedlemskapVurdertTopic }
            it.forEach {  println(it.value) }
        }.onEach {
            consumer.commitAsync()
        }

}