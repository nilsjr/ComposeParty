plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "de.nilsdruyen.composeparty"
    compileSdk = 34
    compileSdkPreview = "UpsideDownCake"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
            jvmTarget = "17"
            languageVersion = "2.0"
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/linux/x64/org/lwjgl/stb/liblwjgl_stb.so.sha1"
        }
    }
}

dependencies {
    with(libs.androidx) {
        implementation(core.ktx)
        implementation(lifecycle)
        implementation(splashscreen)

        implementation(platform("dev.chrisbanes.compose:compose-bom:2023.10.00-alpha02"))
        // Use whichever Compose artifacts you need without a version number
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.runtime:runtime")
        implementation("androidx.compose.foundation:foundation")
        implementation("androidx.compose.animation:animation")
        implementation("androidx.compose.animation:animation-graphics")
        implementation("androidx.compose.material:material")
        implementation("androidx.compose.material:material-icons-extended")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.ui:ui-tooling-preview")
        debugImplementation("androidx.compose.ui:ui-tooling")

        implementation(compose.activity)
        implementation(compose.constraint)
    }

    implementation(libs.accompanist.systemuicontroller)
//    implementation(libs.accompanist.placeholder)
    implementation(libs.accompanist.placeholder.material)

    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.konfetti)

    implementation(libs.timber)
    implementation(libs.skydoves.orbital)

    implementation(libs.graphics.glm)
}