package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.Platform
import com.example.model.SourceType
import com.example.model.WindowsConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("OmniBuild", appName)
  }

  @Test
  fun `verify platform extensions and desktop flag`() {
    assertEquals(".exe", Platform.WINDOWS.extension)
    assertEquals(true, Platform.WINDOWS.isDesktop)
    assertEquals(".apk", Platform.ANDROID.extension)
    assertEquals(false, Platform.ANDROID.isDesktop)
  }

  @Test
  fun `verify windows default config values`() {
    val winConfig = WindowsConfig()
    assertEquals(1280, winConfig.windowWidth)
    assertEquals(720, winConfig.windowHeight)
    assertEquals(true, winConfig.hasSystemTray)
    assertEquals(true, winConfig.isResizable)
  }
}
