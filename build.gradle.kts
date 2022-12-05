plugins {
    id("com.android.application") version "8.0.0-alpha09" apply false
    id("com.android.library") version "8.0.0-alpha09" apply false
    kotlin("android") version "1.7.21" apply false
    id("com.github.ben-manes.versions") version "0.44.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}