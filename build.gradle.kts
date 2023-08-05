plugins {
    id("com.android.application") version "8.2.0-alpha15" apply false
    kotlin("android") version "1.9.0" apply false
    id("com.github.ben-manes.versions") version "0.47.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}