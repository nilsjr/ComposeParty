plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ben.manes.versions)
}

buildscript {
    dependencies {
        // override kotlin gradle plugin version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.0")
    }
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}