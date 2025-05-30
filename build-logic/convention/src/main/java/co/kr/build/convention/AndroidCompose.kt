package co.kr.build.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            add("implementation", platform(libs.findLibrary("androidx.compose.bom").get()))
            add("androidTestImplementation", platform(libs.findLibrary("androidx.compose.bom").get()))
            add("implementation", libs.findBundle("compose").get())
            add("androidTestImplementation", libs.findBundle("android-test-compose").get())
            add("debugImplementation", libs.findBundle("debug-compose").get())
        }
    }
}