package co.kr.mvisample.testing.utils

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import org.robolectric.RuntimeEnvironment

val DefaultRoborazziOptions =
    RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0f),
        recordOptions = RoborazziOptions.RecordOptions(resizeScale = 0.5),
    )

enum class DefaultTestDevices(val description: String, val spec: String) {
    PIXEL4A("pixel4a", "spec:shape=Normal,width=393,height=841,unit=dp,dpi=440"),
    ZFLIP3("zFlip3", "spec:shape=Normal,width=406,height=991,unit=dp,dpi=420")
}

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureMultiDevice(
    screenshotName: String,
    content: @Composable () -> Unit,
    assertion: AndroidComposeTestRule<ActivityScenarioRule<A>, A>.() -> Unit,
) {
    DefaultTestDevices.entries.forEach {
        this.captureForDevice(
            deviceName = it.description,
            deviceSpec = it.spec,
            screenshotName = screenshotName,
            content = content,
            assertion = assertion,
        )
    }
}

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureForDevice(
    deviceName: String,
    deviceSpec: String,
    screenshotName: String,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    content: @Composable () -> Unit,
    assertion: AndroidComposeTestRule<ActivityScenarioRule<A>, A>.() -> Unit,
) {
    val (width, height, dpi) = extractSpecs(deviceSpec)

    RuntimeEnvironment.setQualifiers("w${width}dp-h${height}dp-${dpi}dpi")

    this.activity.setContent {
        // 아래와 같이 LocalInspectionMode 를 사용하면
        // HiltTestActivity SingletonImageLoader 를 설정하지 않아도 될 것 같음
        // CompositionLocalProvider(
            // LocalInspectionMode provides true
        // ) { content() }

        content()
    }

    this.assertion()

    this.onRoot()
        .captureRoboImage(
            filePath = "${screenshotName}_${deviceName}.png",
            roborazziOptions = roborazziOptions
        )
}

private fun extractSpecs(deviceSpec: String): TestDeviceSpecs {
    val specs = deviceSpec.substringAfter("spec:")
        .split(",").map { it.split("=") }.associate { it[0] to it[1] }
    val width = specs["width"]?.toInt() ?: 640
    val height = specs["height"]?.toInt() ?: 480
    val dpi = specs["dpi"]?.toInt() ?: 480
    return TestDeviceSpecs(width, height, dpi)
}

data class TestDeviceSpecs(val width: Int, val height: Int, val dpi: Int)