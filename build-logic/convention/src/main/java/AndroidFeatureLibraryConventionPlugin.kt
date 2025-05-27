@file:Suppress("unused")

import co.kr.build.convention.configureKotlinAndroid
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class AndroidFeatureLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("mvisample.android.library.compose")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig {
                    testInstrumentationRunner = "co.kr.mvisample.testing.CustomTestRunner"
                }
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(17)
            }

            dependencies.apply {
                add("implementation", project(":core:data"))
                add("implementation", project(":core:design"))
                add("implementation", project(":core:common"))
                add("implementation", project(":core:navigation"))
                add("testImplementation", project(":core:testing"))
                add("androidTestImplementation", project(":core:design"))
                add("androidTestImplementation", project(":core:common"))
                add("androidTestImplementation", project(":core:testing"))
            }
        }
    }
}