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
        compilerOptions {
            progressiveMode.set(true)
        }
        kotlinOptions {
            jvmTarget = "17"
            languageVersion = "2.0"
            freeCompilerArgs = listOf(
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

//noinspection UseTomlInstead
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.compose.activity)
    implementation(libs.androidx.compose.constraint)
    implementation(libs.androidx.splashscreen)

    implementation(libs.timber)

    implementation(platform("dev.chrisbanes.compose:compose-bom:2023.12.00-alpha02"))
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

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.placeholder.material)

    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)
    implementation(libs.konfetti)
    implementation(libs.skydoves.orbital)
    implementation(libs.graphics.glm)

    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

    implementation("io.github.klassenkonstantin:physics-layout:0.4.1")
    implementation("net.engawapg.lib:zoomable:1.6.0-beta1")
    implementation("dev.shreyaspatil:capturable:1.0.3")
}