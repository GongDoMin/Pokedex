plugins {
    id("mvisample.android.application")
    id("mvisample.android.application.compose")
    id("mvisample.ksp")
    id("mvisample.serialization")
    id("mvisample.android.hilt")
    id("mvisample.coil")
}

android {
    namespace = "co.kr.mvisample"

    defaultConfig {
        applicationId = "co.kr.mvisample"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {

    implementation(projects.feature)
    implementation(projects.core.design)

    implementation(libs.androidx.runner)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.turbine)
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}