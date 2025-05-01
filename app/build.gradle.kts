plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)

                implementation(kotlin("reflect"))
                implementation(kotlin("scripting-jsr223"))

                implementation(libs.kafka.client)
                implementation(libs.kotlinx.atomicfu)
                // implementation(libs.kotlinx.datetime)

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.kotlinx.datetime)

                implementation(libs.logback.classic)

                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.content.negotiation)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.host.common)
                implementation(libs.ktor.server.resources)
                implementation(libs.ktor.server.auto.head.response)
                implementation(libs.ktor.server.sessions)
                implementation(libs.ktor.server.auth)
                implementation(libs.ktor.server.auth.jwt)
                implementation(libs.ktor.server.cio)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.server.test.host)

            }
        }
    }
}
