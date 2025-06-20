plugins {
    id("mvisample.android.feature.library")
    id("mvisample.android.serialization")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "co.kr.mvisample.feature"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true

            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
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

    implementation(libs.coil.compose)

    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.robolectric)

    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.jUnit)

    testRuntimeOnly(libs.junit.vintage.engine)
}

roborazzi {
    outputDir.set(file("src/screenshots"))

    compare {
        outputDir.set(file("build/outputs/screenshots_comparison"))
    }
}