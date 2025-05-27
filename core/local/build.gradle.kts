plugins {
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("maisample.android.room")
}

android {
    namespace = "co.kr.mvisample.local"
}

dependencies {

    implementation(libs.androidx.runner)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}