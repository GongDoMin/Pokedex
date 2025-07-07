plugins {
    id("mvisample.android.library")
    id("mvisample.ksp")
    id("mvisample.android.hilt")
    id("mvisample.serialization")
    id("mvisample.kotest")
}

android {
    namespace = "co.kr.mvisample.remote"

    buildTypes {
        debug {
            buildConfigField("String", "POKEMON_API", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("String", "POKEMON_IMAGE_URL", "\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/\"")
        }

        release {
            buildConfigField("String", "POKEMON_API", "\"https://pokeapi.co/api/v2/\"")
            buildConfigField("String", "POKEMON_IMAGE_URL", "\"https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    testImplementation(projects.core.testing)

    implementation(libs.androidx.runner)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit.serialization.converter)

    testImplementation(libs.junit)
}