plugins {
    id("com.android.application") version "8.6.0-alpha02" apply false
    kotlin("android") version "2.0.0-RC3" apply false
    id("com.github.ben-manes.versions") version "0.51.0"
    alias(libs.plugins.kotlin.compose) apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}