plugins {
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("mvisample.android.serialization")
    id("maisample.android.room")
    id("mvisample.android.library.compose")
}

android {
    namespace = "co.kr.mvisample.testing"
}

dependencies {

    implementation(projects.feature)
    implementation(projects.core.local)
    implementation(projects.core.remote)
    implementation(projects.core.data)

    implementation(libs.androidx.runner)
    
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.ui.test.junit4.android)
    
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.coil.test)

    implementation(libs.roborazzi)
    implementation(libs.roborazzi.compose)
    implementation(libs.roborazzi.jUnit)

    implementation(libs.robolectric)
}