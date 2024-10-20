import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "de.nilsdruyen.composeparty"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.nilsdruyen.composeparty"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        compilerOptions {
            progressiveMode.set(true)
            jvmTarget.set(JvmTarget.JVM_21)
            languageVersion.set(KotlinVersion.KOTLIN_2_0)
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xcontext-receivers",
                )
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/linux/x64/org/lwjgl/**"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.constraint)
    implementation(libs.androidx.splashscreen)

    implementation(libs.timber)

    //noinspection UseTomlInstead
    implementation(platform(libs.androidx.compose.bom))
    // Use whichever Compose artifacts you need without a version number
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.animation:animation-graphics")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation(libs.accompanist.placeholder.material)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.barcode.scanning)

    implementation(libs.haze)
    implementation(libs.haze.materials)
    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.konfetti)
    implementation(libs.skydoves.orbital)
    implementation(libs.graphics.glm)

    implementation(libs.androidx.media.exoplayer)
    implementation(libs.androidx.media.ui)

    implementation(libs.physics.layout)
    implementation(libs.zoomable)
    implementation(libs.captureable)
}