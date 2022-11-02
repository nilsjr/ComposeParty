plugins {
    id("com.android.application") version "8.0.0-alpha07" apply false
    id("com.android.library") version "8.0.0-alpha07" apply false
    kotlin("android") version "1.7.20" apply false
    id("com.github.ben-manes.versions") version "0.43.0"
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}