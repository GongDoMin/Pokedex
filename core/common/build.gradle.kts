plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
}

android {
    namespace = "co.kr.mvisample.common"

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
    implementation(project(":core:data"))
    implementation(project(":core:design"))

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runner)

    // JUnit
    testImplementation(libs.junit)
}