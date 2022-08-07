plugins {
    id("com.android.application") version "7.4.0-alpha09" apply false
    id("com.android.library") version "7.4.0-alpha09" apply false
    kotlin("android") version "1.7.10" apply false
    id("com.github.ben-manes.versions") version "0.42.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}