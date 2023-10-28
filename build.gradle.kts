plugins {
    id("com.android.application") version "8.3.0-alpha11" apply false
    kotlin("android") version "1.9.20-RC2" apply false
    id("com.github.ben-manes.versions") version "0.49.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}