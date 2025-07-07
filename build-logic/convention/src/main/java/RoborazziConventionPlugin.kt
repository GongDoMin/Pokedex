@file:Suppress("unused")

import co.kr.build.convention.libs
import com.android.tools.r8.internal.io
import io.github.takahirom.roborazzi.RoborazziExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class RoborazziConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.github.takahirom.roborazzi")
            }

            dependencies {
                add("testImplementation", libs.findLibrary("roborazzi").get())
                add("testImplementation", libs.findLibrary("roborazzi-compose").get())
                add("testImplementation", libs.findLibrary("roborazzi-jUnit").get())
            }

            extensions.configure<RoborazziExtension> {
                outputDir.set(file("src/screenshots"))

                compare {
                    outputDir.set(file("build/outputs/screenshots_comparison"))
                }
            }
        }
    }
}