plugins {
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

    implementation(projects.feature)
    implementation(projects.core.local)
    implementation(projects.core.remote)
    implementation(projects.core.data)

    implementation(libs.androidx.runner)
    
    implementation(libs.androidx.paging.compose)
}