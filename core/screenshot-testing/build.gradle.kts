plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
}

android {
    namespace = "co.kr.mvisample.testing"

    packaging {
        resources.excludes.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
}

dependencies {

    implementation(libs.androidx.ui.test.junit4.android)

    implementation(libs.roborazzi)
    implementation(libs.roborazzi.compose)
    implementation(libs.roborazzi.jUnit)

    implementation(libs.robolectric)
}