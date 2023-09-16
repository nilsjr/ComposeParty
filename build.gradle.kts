plugins {
    id("com.android.application") version "8.3.0-alpha04" apply false
    kotlin("android") version "1.9.20-Beta" apply false
    id("com.github.ben-manes.versions") version "0.48.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}