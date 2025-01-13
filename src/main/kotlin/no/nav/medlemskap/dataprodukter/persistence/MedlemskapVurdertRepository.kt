package no.nav.medlemskap.dataprodukter.persistence

import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.medlemskap.dataprodukter.domain.Brudd
import no.nav.medlemskap.dataprodukter.domain.Vurdering
import javax.sql.DataSource

interface VurdertRepository {
    fun finnVurdering(fnr: String): List<Vurdering>
    fun lagreVurdering(vurdering: Vurdering)
    fun finnBrudd(fnr: String) : List<Brudd>
}

class PostgresVurdertRepository(val dataSource: DataSource) : VurdertRepository {
    val INSERT_VURDERING = "INSERT INTO vurdering (id,fnr, fom,tom,status) VALUES(?, ?, ?, ?, ?)"
    val FIND_BY_FNR = "select * from vurdering where fnr = ?"
    val FIND_BRUDD = "select * from brudd where id = ?"
    val INSERT_BRUDD = "INSERT INTO brudd (id,regel_id, beskrivelse) VALUES(?, ?, ?)"

    override fun finnVurdering(fnr: String): List<Vurdering> {

        val vurderinger =  kotliquery.using(sessionOf(dataSource)) {
            it.run(queryOf(FIND_BY_FNR, fnr).map(toVurderingDao).asList)
        }

        vurderinger.forEach{
            val brudd = finnBrudd(it.id)
            it.brudd=brudd
        }
        return vurderinger
    }

    override fun finnBrudd(id: String): List<Brudd> {
        return kotliquery.using(sessionOf(dataSource)) {
            it.run(queryOf(FIND_BRUDD, id).map(toBruddDao).asList)
        }
    }

    override fun lagreVurdering(vurderingDao: Vurdering) {
        kotliquery.using(sessionOf(dataSource)) { session ->
            session.transaction {
                it.run(
                    queryOf(
                        INSERT_VURDERING,
                        vurderingDao.id,
                        vurderingDao.fnr,
                        vurderingDao.fom,
                        vurderingDao.tom,
                        vurderingDao.status
                    ).asExecute
                )
                vurderingDao.brudd.forEach{ t->
                    it.run(
                        queryOf(
                            INSERT_BRUDD,
                            t.id,
                            t.rege_id,
                            t.beskrivelse
                        ).asExecute
                    )
                }
            }

        }
    }

    val toVurderingDao: (Row) -> Vurdering = { row ->
        Vurdering(
            row.string("id"),
            row.string("fnr"),
            row.localDate("fom"),
            row.localDate("tom"),
            row.string("status")
        )
    }
    val toBruddDao: (Row) -> Brudd = { row ->
        Brudd(
            row.string("id"),
            row.string("regel_id"),
            row.string("beskrivelse")
        )
    }
}
