plugins {
    id("mvisample.android.library")
    id("mvisample.android.library.compose")
    id("mvisample.android.serialization")
}

android {
    namespace = "co.kr.mvisample.navigation"

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

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runner)

    // navigation
    implementation(libs.androidx.navigation.compose)
}