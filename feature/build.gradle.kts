plugins {
    id("mvisample.android.feature.library")
    id("mvisample.ksp")
    id("mvisample.serialization")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
    id("mvisample.coil")
}

android {
    namespace = "co.kr.mvisample.feature"
}

dependencies {

    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.navigation.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)


}