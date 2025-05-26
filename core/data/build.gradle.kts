plugins {
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.data"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    // dependencies
    implementation(project(":core:remote"))
    implementation(project(":core:local"))
    testImplementation(project(":core:testing"))

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runner)

    // JUnit
    testImplementation(libs.junit)

    // paging
    implementation(libs.androidx.paging.compose)

    // turbin
    testImplementation(libs.turbine)
}