plugins {
    id("mvisample.android.feature.library")
    id("mvisample.android.serialization")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.feature"
}

dependencies {

    testImplementation(projects.turbino)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    testImplementation(libs.junit)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.androidx.navigation.testing)

    implementation(libs.androidx.paging.compose)

    implementation(libs.coil.compose)

    implementation(libs.kotlinx.collections.immutable)
}