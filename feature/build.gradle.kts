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
    testImplementation(projects.turbine)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(projects.core.testing)

    testImplementation(libs.junit)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.androidx.navigation.testing)

    implementation(libs.androidx.paging.compose)
}