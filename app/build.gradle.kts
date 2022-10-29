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

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
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
            freeCompilerArgs = listOf(
                "-progressive",
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xcontext-receivers",
            )
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.lifecycle)

    implementation(libs.composeUi)
    implementation(libs.composeActivity)
    implementation(libs.composeMaterial3)
    implementation(libs.composeAnimationGraphics)
    implementation(libs.composeMaterialIcons)
    implementation(libs.androidx.compose.constraint)
    implementation(libs.coilCompose)

    debugImplementation(libs.composeUiTooling)
    implementation(libs.composeUiToolingPreview)

    implementation(libs.accompanist.systemuicontroller)

    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.airbnb.android:lottie-compose:5.2.0")
    implementation("com.github.skydoves:orbital:0.2.2")
}