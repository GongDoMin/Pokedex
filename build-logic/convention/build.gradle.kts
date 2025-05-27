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

        register("AndroidKotestPlugin") {
            id = "mvisample.kotest"
            implementationClass = "AndroidKotestConventionPlugin"
        }

        register("AndroidSerializationPlugin") {
            id = "mvisample.android.serialization"
            implementationClass = "AndroidSerializationConventionPlugin"
        }

        register("AndroidRoomPlugin") {
            id = "maisample.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}