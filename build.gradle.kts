plugins {
    id("com.android.application") version "7.4.0-alpha02" apply false
    id("com.android.library") version "7.4.0-alpha02" apply false
    kotlin("android") version "1.6.21" apply false
    id("com.github.ben-manes.versions") version "0.42.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}