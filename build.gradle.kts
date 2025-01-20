val ktorVersion = "3.0.3"
val konfigVersion = "1.6.10.0"
val kafkaVersion = "3.9.0"
val jacksonVersion = "2.14.1"
val kotlinLoggerVersion = "1.8.3"
val resilience4jVersion = "1.5.0"
val logstashVersion = "6.4"
val logbackVersion = "1.5.15"
val mainClass = "no.nav.medlemskap.dataprodukter.ApplicationKt"
val flywayVersion = "9.22.3"
val postgresVersion = "42.7.4"
val hikariVersion = "6.2.1"
val testcontainerVersion = "1.20.4"
val kotliqueryVersion = "1.3.1"


plugins {
    kotlin("jvm") version "2.1.0"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "no.nav.medlemskap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggerVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.slf4j:slf4j-api:2.0.15")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("com.natpryce:konfig:$konfigVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggerVersion")
    //Kafka-avhengigheter
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    //database
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("com.github.seratch:kotliquery:$kotliqueryVersion")
    // 2.8.0 er tilgjengelig, burde kanskje oppdatere
    testImplementation(platform("org.junit:junit-bom:5.7.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.testcontainers:postgresql:$testcontainerVersion")
    testImplementation ("org.testcontainers:junit-jupiter:1.16.0")
}

kotlin {
    jvmToolchain(21)
}
tasks.withType<Test> {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
    })
}
tasks.withType<Wrapper> {
    gradleVersion = "8.12"
}
tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = "21" // Set JVM target for Kotlin code to 21
    }
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to mainClass
                )
            )
        }
    }
    java{
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

    }
    test {
        useJUnitPlatform()
        jvmArgs = listOf("-Dnet.bytebuddy.experimental=true")
        java.targetCompatibility = JavaVersion.VERSION_21
        java.sourceCompatibility = JavaVersion.VERSION_21
    }

}

application {
    mainClass.set("no.nav.medlemskap.dataprodukter.ApplicationKt")
}
