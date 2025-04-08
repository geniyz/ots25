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
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit)
            }
        }
    }
}
