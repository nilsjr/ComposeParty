plugins {
    id("com.android.application") version "8.4.0-alpha04" apply false
    kotlin("android") version "2.0.0-Beta3" apply false
    id("com.github.ben-manes.versions") version "0.51.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}