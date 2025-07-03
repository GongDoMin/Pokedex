plugins {
    id("mvisample.android.library")
    id("mvisample.ksp")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.data"
}

dependencies {

    implementation(projects.core.remote)
    implementation(projects.core.local)

    implementation(libs.androidx.runner)
    implementation(libs.androidx.paging.compose)

    testImplementation(projects.core.testing)
    testImplementation(projects.turbine)

    testImplementation(libs.junit)
}