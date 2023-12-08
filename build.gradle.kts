plugins {
    id("com.android.application") version "8.3.0-alpha17" apply false
    kotlin("android") version "1.9.21" apply false
    id("com.github.ben-manes.versions") version "0.50.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}