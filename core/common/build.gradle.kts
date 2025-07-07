plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
}

android {
    namespace = "co.kr.mvisample.common"
}

dependencies {

    implementation(projects.core.data)
    implementation(projects.core.design)

    implementation(libs.androidx.runner)

    testImplementation(libs.junit)
}