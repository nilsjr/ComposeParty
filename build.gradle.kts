plugins {
    id("com.android.application") version "8.4.0-alpha09" apply false
    kotlin("android") version "2.0.0-Beta4" apply false
    id("com.github.ben-manes.versions") version "0.51.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}