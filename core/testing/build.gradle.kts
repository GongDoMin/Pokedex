plugins {
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("mvisample.android.serialization")
    id("maisample.android.room")
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
}