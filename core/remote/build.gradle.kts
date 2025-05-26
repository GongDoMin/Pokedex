import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

plugins {
    alias(libs.plugins.jetbrains.kotlin.kapt)
    id("mvisample.android.library")
    id("mvisample.android.hilt")
    id("mvisample.android.serialization")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.remote"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String", "POKEMON_API", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("String", "POKEMON_IMAGE_URL", "\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/\"")
        }

        release {
            buildConfigField("String", "POKEMON_API", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("String", "POKEMON_IMAGE_URL", "\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    // dependencies
    testImplementation(project(":core:testing"))

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runner)

    // JUnit
    testImplementation(libs.junit)

    // retrofit and okhttp
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit.serialization.converter)
}