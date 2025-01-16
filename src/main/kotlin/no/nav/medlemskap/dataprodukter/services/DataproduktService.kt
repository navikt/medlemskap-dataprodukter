package no.nav.medlemskap.dataprodukter.services

import no.nav.medlemskap.dataprodukter.domain.VurdertMessageRecord
import no.nav.medlemskap.dataprodukter.persistence.VurdertRepository

class DataproduktService(vurdertRepository: VurdertRepository) {
    fun handle(vurdertMessageRecord: VurdertMessageRecord){
        println(vurdertMessageRecord.value)
    }
}