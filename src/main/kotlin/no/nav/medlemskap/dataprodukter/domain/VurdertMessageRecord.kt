package no.nav.medlemskap.dataprodukter.domain

import java.time.LocalDateTime


data class VurdertMessageRecord(val partition:Int, val offset:Long, val value : String, val key:String?, val topic:String, val timestamp: LocalDateTime, val timestampType:String)

