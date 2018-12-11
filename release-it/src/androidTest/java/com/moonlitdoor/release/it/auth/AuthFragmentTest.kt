package com.moonlitdoor.release.it.auth

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.moonlitdoor.release.it.App
import com.moonlitdoor.release.it.HostRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthFragmentTest {

  @Rule
  @JvmField
  val port = HostRule()

  @Rule
  @JvmField
  val activity: ActivityTestRule<AuthActivity> = object : ActivityTestRule<AuthActivity>(AuthActivity::class.java, false, false) {
    override fun afterActivityLaunched() {
      onWebView().forceJavascriptEnabled()
    }
  }

  @Test
  fun useAppContext() {
    activity.launchActivity(null)
    val appContext: App = ApplicationProvider.getApplicationContext()
    Assert.assertEquals("com.moonlitdoor.release.it", appContext.packageName)
//    Thread.sleep(5000)
//    onWebView().withElement(findElement(Locator.NAME, "login")).perform(webClick()) //   .perform(typeText("test"))
//    Thread.sleep(5000)
//    onWebView().withElement(findElement(Locator.ID, "password")).perform(webClick()) //   .perform(typeText("test"))
//    Thread.sleep(5000)
//    onWebView().withElement(findElement(Locator.NAME, "commit")).perform(webClick()) //   .perform(typeText("test"))
  }
}