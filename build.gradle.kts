import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    kotlin("jvm") version "2.0.0"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("no.nav.helse.flex.AppKt")
}

val kluentVersion = "1.73"
val junitVersion = "5.10.3"
val logbackVersion = "1.5.6"
val jacksonVersion = "2.17.1"
val logstashEncoderVersion = "7.4"

dependencies {
    api(platform("com.google.cloud:libraries-bom:26.1.3"))
    implementation(kotlin("stdlib"))
    implementation("com.google.cloud:google-cloud-bigquery")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
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
}

tasks.withType<Jar>().configureEach {
    manifest { attributes["Main-Class"] = application.mainClass }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
