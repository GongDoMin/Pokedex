@file:Suppress("unused")

import co.kr.build.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
                apply("org.jetbrains.kotlin.kapt")
            }

            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())
                add("implementation", libs.findLibrary("hilt-android-testing").get())
                add("kapt", libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}