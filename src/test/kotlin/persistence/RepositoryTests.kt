package persistence

import no.nav.medlemskap.dataprodukter.domain.Brudd
import no.nav.medlemskap.dataprodukter.domain.Vurdering
import no.nav.medlemskap.dataprodukter.persistence.DataSourceBuilder
import no.nav.medlemskap.dataprodukter.persistence.PostgresVurdertRepository
import org.junit.Ignore

import org.junit.jupiter.api.Assertions

import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

import java.time.LocalDate
import java.util.UUID
import java.util.logging.Level
import java.util.logging.LogManager


class MyPostgreSQLContainer(imageName: String) : PostgreSQLContainer<MyPostgreSQLContainer>(imageName)
@org.testcontainers.junit.jupiter.Testcontainers
@Ignore
class RepositoryTests : AbstractContainerDatabaseTest() {

    init {
        // Postgres JDBC driver uses JUL; disable it to avoid annoying, irrelevant, stderr logs during connection testing
        LogManager.getLogManager().getLogger("").level = Level.OFF
    }
    companion object {
        // will be shared between test methods
        @Container
        private val postgresqlContainer     = MyPostgreSQLContainer("postgres:17")
            .withDatabaseName("dataprodukter")
            .withUsername("postgres")
            .withPassword("test")
    }


    @Test
    fun `lagre vurdering med relasjoner`() {
        postgresqlContainer.withUrlParam("user", postgresqlContainer.username)
        postgresqlContainer.withUrlParam("password", postgresqlContainer.password)
        val dsb = DataSourceBuilder(mapOf("DB_JDBC_URL" to postgresqlContainer.jdbcUrl))
        dsb.migrate();
        val dataSource = dsb.getDataSource()
        val repo = PostgresVurdertRepository(dsb.getDataSource())
        val vurderingId = UUID.randomUUID().toString()
        val listeAvBrudd = listOf<Brudd>(
            Brudd(vurderingId,"REGEL_19_1","UDI svarer uavklart"),
            Brudd(vurderingId,"REGEL_C","Innslag i JOARK")
        )
        val vurdering = Vurdering(
            id= vurderingId,
            fnr="1",
            fom= LocalDate.now(),
            tom= LocalDate.now(),
            status="UAVKLART",
            brudd = listeAvBrudd)


        repo.lagreVurdering(vurdering)
        val dbResponse = repo.finnVurdering("1")
        Assertions.assertTrue(dbResponse.isNotEmpty())
        Assertions.assertTrue(dbResponse.first().brudd.size==2)
    }
        
    }

