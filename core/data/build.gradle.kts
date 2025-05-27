plugins {
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.data"
}

dependencies {

    implementation(projects.core.remote)
    implementation(projects.core.local)
    testImplementation(projects.core.testing)

    implementation(libs.androidx.runner)

    testImplementation(libs.junit)

    implementation(libs.androidx.paging.compose)
    
    testImplementation(libs.turbine)
}