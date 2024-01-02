plugins {
    id("com.android.application") version "8.4.0-alpha01" apply false
    kotlin("android") version "2.0.0-Beta2" apply false
    id("com.github.ben-manes.versions") version "0.50.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}