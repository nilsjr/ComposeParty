plugins {
    id("com.android.application") version "8.0.0-alpha01" apply false
    id("com.android.library") version "8.0.0-alpha01" apply false
    kotlin("android") version "1.7.10" apply false
    id("com.github.ben-manes.versions") version "0.42.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}