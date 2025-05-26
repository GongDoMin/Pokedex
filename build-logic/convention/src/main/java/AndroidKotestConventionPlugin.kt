@file:Suppress("unused")

import co.kr.build.convention.libs
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidKotestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests.all { it.useJUnitPlatform() }
                }
            }

            dependencies {
                add("testImplementation", libs.findLibrary("kotest-runner").get())
                add("testImplementation", libs.findLibrary("kotest-assertions").get())
                add("testImplementation", libs.findLibrary("kotest-property").get())
            }
        }
    }
}