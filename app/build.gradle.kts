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
            excludes += "META-INF/INDEX.LIST"
        }
    }
}

dependencies {
    with(libs.androidx) {
        implementation(core.ktx)
        implementation(lifecycle)
        implementation(splashscreen)

        implementation(compose.activity)
        implementation(compose.ui)
        implementation(compose.material3)
        implementation(compose.material.icons)
        implementation(compose.animation)
        implementation(compose.animation.graphics)
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
//    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.3")
}