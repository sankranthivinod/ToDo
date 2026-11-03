import java.util.Properties

val versionPropsFile = file("version.properties")
val versionProps = Properties().apply {
    if (versionPropsFile.exists()) {
        versionPropsFile.inputStream().use { load(it) }
    } else {
        setProperty("VERSION_CODE", "2")
        setProperty("VERSION_NAME", "1.0.1")
        versionPropsFile.outputStream().use { store(it, "Version Properties") }
    }
}

val isReleaseBuild = gradle.startParameter.taskNames.any { it.contains("Release", ignoreCase = true) }

var appVersionCode = versionProps.getProperty("VERSION_CODE", "2").toInt()
var appVersionName = versionProps.getProperty("VERSION_NAME", "1.0.1")

if (isReleaseBuild) {
    appVersionCode += 1
    val parts = appVersionName.split(".")
    if (parts.size == 3) {
        val patch = (parts[2].toIntOrNull() ?: 0) + 1
        appVersionName = "${parts[0]}.${parts[1]}.$patch"
    }
    versionProps.setProperty("VERSION_CODE", appVersionCode.toString())
    versionProps.setProperty("VERSION_NAME", appVersionName)
    versionPropsFile.outputStream().use { versionProps.store(it, "Auto-incremented for Release Build") }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.sankranthivinod.todo"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.sankranthivinod.todo"
        minSdk = 24
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("C:/Users/vinod/keystore/release.keystore")
            storePassword = "Krishna@963"
            keyAlias = "vinod963"
            keyPassword = "Krishna@963"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
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
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.core.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}