plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "de.nilsdruyen.composeparty"
    compileSdk = 33

    defaultConfig {
        applicationId = "de.nilsdruyen.composeparty"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.8"
            freeCompilerArgs = listOf(
                "-progressive",
                "-opt-in=kotlin.RequiresOptIn",
                "-Xcontext-receivers",
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

dependencies {
    with(libs.androidx) {
        implementation(core.ktx)
        implementation(lifecycle)
        implementation(splashscreen)

        api(platform("dev.chrisbanes.compose:compose-bom:2023.02.00-beta01"))

        // Use whichever Compose artifacts you need without a version number
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.runtime:runtime")
        implementation("androidx.compose.foundation:foundation")
        implementation("androidx.compose.animation:animation")
        implementation("androidx.compose.animation:animation-graphics")
        implementation("androidx.compose.material:material-icons-extended")
        implementation("androidx.compose.material3:material3")

        implementation(compose.activity)
        implementation(compose.constraint)

        debugImplementation(compose.ui.tooling)
        implementation(compose.ui.tooling.preview)
    }

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.konfetti)

    implementation(libs.timber)
    implementation(libs.skydoves.orbital)

    implementation(libs.graphics.glm)
}