package no.nav.medlemskap.dataprodukter.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Collections.emptyList


data class VurdertMessageRecord(val partition:Int, val offset:Long, val value : String, val key:String?, val topic:String, val timestamp: LocalDateTime, val timestampType:String)

data class Vurdering (
val id :String,
val fnr :String,
val fom : LocalDate,
val tom : LocalDate,
val status :String,
var brudd: List<Brudd> = emptyList<Brudd>()
)

data class Brudd (
    val id :String,
    val rege_id :String,
    val beskrivelse : String
)