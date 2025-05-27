plugins {
    alias(libs.plugins.jetbrains.kotlin.kapt)
    id("mvisample.android.application")
    id("mvisample.android.application.compose")
    id("mvisample.android.serialization")
    id("mvisample.android.hilt")
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
    implementation(projects.core.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.runner)

    // JUnit
    testImplementation(libs.junit)

    // android test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
}