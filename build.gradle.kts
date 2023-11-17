plugins {
    id("com.android.application") version "8.3.0-alpha14" apply false
    kotlin("android") version "2.0.0-Beta1" apply false
    id("com.github.ben-manes.versions") version "0.50.0"
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}