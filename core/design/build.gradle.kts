plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
}

android {
    namespace = "co.kr.mvisample.design"
}

dependencies {

    implementation(libs.androidx.runner)

    implementation(libs.androidx.navigation.compose)
}