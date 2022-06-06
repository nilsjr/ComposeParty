plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkPreview = "Tiramisu"

    defaultConfig {
        applicationId = "de.nilsdruyen.composeparty"
        minSdk = 26
        targetSdkPreview = "Tiramisu"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
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
    namespace = "de.nilsdruyen.composeparty"
}

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.lifecycle)

    implementation(libs.composeUi)
    implementation(libs.composeActivity)
    implementation(libs.composeMaterial3)
    debugImplementation(libs.composeUiTooling)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeAnimationGraphics)
    implementation(libs.composeMaterialIcons)

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.9-beta")
}