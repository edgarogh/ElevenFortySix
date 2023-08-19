package bzh.edgar.elevenfortysix

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.cleanstatusbar.BluetoothState
import tools.fastlane.screengrab.cleanstatusbar.CleanStatusBar
import tools.fastlane.screengrab.cleanstatusbar.MobileDataType
import tools.fastlane.screengrab.locale.LocaleTestRule

@RunWith(JUnit4::class)
class FastlaneScreenshots {

    @ClassRule
    val localeTestRule = LocaleTestRule()

    @Rule
    var activityRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    @BeforeClass
    fun beforeAll() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        CleanStatusBar()
            .setMobileNetworkDataType(MobileDataType.LTE)
            .setBluetoothState(BluetoothState.DISCONNECTED)
            .enable()
    }

    @AfterClass
    fun afterAll() {
        CleanStatusBar.disable()
    }

    @Test
    fun testMainScreenshot() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        onView(withId(R.id.clock)).check(matches(isDisplayed()))
        Screengrab.screenshot("main_activity")
    }

}
