plugins {
    id("com.android.application") version "8.1.0-alpha04" apply false
    kotlin("android") version "1.8.10" apply false
    id("com.github.ben-manes.versions") version "0.44.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}