plugins {
    id("com.android.application") version "8.2.0-alpha09" apply false
    kotlin("android") version "1.9.0-Beta" apply false
    id("com.github.ben-manes.versions") version "0.47.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}