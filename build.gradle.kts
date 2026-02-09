import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
    id("com.gradleup.shadow") version "8.3.8"
}

repositories {
    mavenCentral()
}

val kluentVersion = "1.73"
val jacksonVersion = "2.21.0"
val junitVersion = "6.0.2"

dependencies {
    implementation(platform("com.google.cloud:libraries-bom:26.75.0"))
    implementation("com.google.cloud:google-cloud-bigquery")
    implementation("ch.qos.logback:logback-classic:1.5.24")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Forhindrer bruk av Gradles innebygde launcher med annen versjon.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
}

ktlint {
    version.set("1.5.0")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
        if (System.getenv("CI") == "true") {
            allWarningsAsErrors.set(true)
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading")
        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
            exceptionFormat = FULL
        }
        failFast = false
    }
    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        isZip64 = true
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "no.nav.helse.flex.AppKt",
                ),
            )
        }
    }
}
