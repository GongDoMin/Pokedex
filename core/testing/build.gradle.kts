plugins {
    alias(libs.plugins.jetbrains.kotlin.kapt)
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("mvisample.android.serialization")
    id("maisample.android.room")
}

android {
    namespace = "co.kr.mvisample.testing"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    // dependencies
    implementation(project(":feature"))
    implementation(project(":core:local"))
    implementation(project(":core:remote"))
    implementation(project(":core:data"))

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runner)

    // paging
    implementation(libs.androidx.paging.compose)
}