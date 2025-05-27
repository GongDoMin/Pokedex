plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
    id("mvisample.android.serialization")
}

android {
    namespace = "co.kr.mvisample.navigation"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation(libs.androidx.runner)

    implementation(libs.androidx.navigation.compose)
}