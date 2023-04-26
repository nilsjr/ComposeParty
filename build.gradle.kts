plugins {
    id("com.android.application") version "8.2.0-alpha01" apply false
    kotlin("android") version "1.8.21" apply false
    id("com.github.ben-manes.versions") version "0.46.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}