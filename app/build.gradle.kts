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

                implementation(libs.logback.classic)

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}
