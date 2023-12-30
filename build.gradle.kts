plugins {
    id("com.android.application") version "8.4.0-alpha01" apply false
    kotlin("android") version "1.9.22" apply false
    id("com.github.ben-manes.versions") version "0.50.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}