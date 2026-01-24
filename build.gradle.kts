plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ben.manes.versions)
}

buildscript {
    dependencies {
        // override kotlin gradle plugin version
        classpath(libs.kotlin.gradlePlugin)
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}