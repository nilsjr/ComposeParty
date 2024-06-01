plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.androidGradle) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ben.manes.versions)
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}