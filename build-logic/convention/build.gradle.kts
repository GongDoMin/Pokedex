plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    implementation(libs.roborazzi.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("AndroidApplicationPlugin") {
            id = "mvisample.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("AndroidApplicationComposePlugin") {
            id = "mvisample.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("AndroidLibraryPlugin") {
            id = "mvisample.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("AndroidFeatureLibraryPlugin") {
            id = "mvisample.android.feature.library"
            implementationClass = "AndroidFeatureLibraryConventionPlugin"
        }

        register("AndroidLibraryComposePlugin") {
            id = "mvisample.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("AndroidHiltPlugin") {
            id = "mvisample.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("KotestPlugin") {
            id = "mvisample.kotest"
            implementationClass = "KotestConventionPlugin"
        }

        register("SerializationPlugin") {
            id = "mvisample.serialization"
            implementationClass = "SerializationConventionPlugin"
        }

        register("KspPlugin") {
            id = "mvisample.ksp"
            implementationClass = "KspConventionPlugin"
        }

        register("CoilPlugin") {
            id = "mvisample.coil"
            implementationClass = "CoilConventionPlugin"
        }

        register("RoborazziPlugin") {
            id = "mvisample.roborazzi"
            implementationClass = "RoborazziConventionPlugin"
        }

        register("AndroidRoomPlugin") {
            id = "maisample.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}