package no.nav.sandkasse


import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.medlemskap.dataprodukter.config.Configuration
import no.nav.medlemskap.dataprodukter.config.KafkaConfig
import no.nav.medlemskap.dataprodukter.config.PlainStrategy
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun main(args: Array<String>) {
    val securityStrategy: KafkaConfig.SecurityStrategy = PlainStrategy(environment = System.getenv())
    val value = 1652174197864
    val date = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(value), ZoneId.systemDefault())
    println(date)

    val consumer: KafkaConsumer<String, String> = KafkaConsumer<String, String>(mapOf(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to Configuration.KafkaConfig().bootstrapServers,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        CommonClientConfigs.CLIENT_ID_CONFIG to "client_id",
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.GROUP_ID_CONFIG to "medlemskap.sandkasse",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to "false",
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 10,

        ) + securityStrategy.securityConfig())
    val vurdert_topic = "medlemskap.medlemskap-vurdert"

    consumer.subscribe(listOf(vurdert_topic))

    val pollTimeout = Duration.ofSeconds(2)
    while (true) {
        val records = consumer.poll(pollTimeout)

        if (!records.isEmpty) {
            records.map { transform(it) }
            records.forEach {
                handleMessage(it)
            }

            //consumer.commitSync()
        } else {
            println("ingen meldinger funnet på kø")
        }
    }
}

fun transform(it: ConsumerRecord<String, String>?) {

}

fun handleMessage(it: ConsumerRecord<String, String>?) {
    val timestamp = LocalDateTime.ofInstant(
        it?.let { it1 -> Instant.ofEpochMilli(it1.timestamp()) }, ZoneId.systemDefault())

    val json = it?.value()
    val JsonNode = ObjectMapper().readTree(json)
    println("recieved message")

    }

    //println(it.value())


enum class Soknadstatus {
    NY,
    SENDT,
    FREMTIDIG,
    UTKAST_TIL_KORRIGERING,
    KORRIGERT,
    AVBRUTT,
    UTGATT,
    SLETTET
}
