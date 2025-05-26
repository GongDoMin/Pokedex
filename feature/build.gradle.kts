plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
    id("mvisample.android.serialization")
    id("mvisample.android.hilt")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.feature"

    defaultConfig {
        testInstrumentationRunner = "co.kr.mvisample.testing.CustomTestRunner"
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
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootProject.file(".").absolutePath}/compose-metrics",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootProject.file(".").absolutePath}/compose-reports"
        )
    }
}

dependencies {

    // dependencies
    implementation(project(":core:data"))
    implementation(project(":core:design"))
    implementation(project(":core:navigation"))
    implementation(project(":core:common"))
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:design"))
    androidTestImplementation(project(":core:common"))

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runner)

    // android test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // JUnit
    testImplementation(libs.junit)

    // navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.androidx.navigation.testing)

    // paging
    implementation(libs.androidx.paging.compose)

    // coil
    implementation(libs.coil.compose)

    // immutable collections
    implementation(libs.kotlinx.collections.immutable)

    // turbin
    testImplementation(libs.turbine)
}