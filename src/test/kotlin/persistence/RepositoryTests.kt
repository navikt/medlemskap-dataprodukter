package persistence

import no.nav.medlemskap.dataprodukter.persistence.DataSourceBuilder

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import java.time.LocalDate
import java.util.*
import java.util.logging.Level
import java.util.logging.LogManager


class MyPostgreSQLContainer(imageName: String) : PostgreSQLContainer<MyPostgreSQLContainer>(imageName)
@org.testcontainers.junit.jupiter.Testcontainers
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
    fun `lagre medlemskap vurdering`() {
        postgresqlContainer.withUrlParam("user", postgresqlContainer.username)
        postgresqlContainer.withUrlParam("password", postgresqlContainer.password)
        val dsb = DataSourceBuilder(mapOf("DB_JDBC_URL" to postgresqlContainer.jdbcUrl))
        dsb.migrate();
        val v = dsb.getDataSource().connection.createStatement().execute("SELECT * FROM vurdering")


    }
    }