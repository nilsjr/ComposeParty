pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://raw.githubusercontent.com/kotlin-graphics/mary/master")
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}
rootProject.name = "ComposeParty"

include(":app")

enableFeaturePreview("VERSION_CATALOGS")