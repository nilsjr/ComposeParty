plugins {
    id("com.android.application") version "8.3.0-alpha02" apply false
    kotlin("android") version "1.9.10" apply false
    id("com.github.ben-manes.versions") version "0.48.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}