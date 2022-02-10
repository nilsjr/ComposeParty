plugins {
    id("com.android.application") version "7.1.1" apply false
    id("com.android.library") version "7.1.1" apply false
    kotlin("android") version "1.6.10" apply false
    id("com.github.ben-manes.versions") version "0.42.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}