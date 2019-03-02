package com.moonlitdoor.release.it.auth

import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.moonlitdoor.release.it.BuildConfig
import com.moonlitdoor.release.it.HOST
import com.moonlitdoor.release.it.WebServer
import com.moonlitdoor.release.it.WebServerRule
import com.moonlitdoor.release.it.navigation.NavigationActivity
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.koin.test.declare

@RunWith(AndroidJUnit4::class)
class AuthFragmentTest : KoinTest {

  val code = "ACCESS_CODE"

  @Rule
  @JvmField
  val serverRule = WebServerRule(default = {
    when (it.path) {
      "/login/oauth/authorize?client_id=${BuildConfig.GITHUB_CLIENT_ID}&scope=repo,read:user,read:org" -> MockResponse().setResponseCode(
          302).setHeader("Location", "https://www.example.com/apps/releaseit/oauth/callback?code=$code")
      "/login/oauth/access_token?client_id=${BuildConfig.GITHUB_CLIENT_ID}&client_secret=${BuildConfig.GITHUB_CLIENT_SECRET}&code=$code" -> MockResponse().setBody(
          "{\"access_token\":\"fake_token\"}")
      else -> MockResponse().setResponseCode(WebServerRule.HTTP_501_NOT_IMPLEMENTED)
    }
  })

  @Rule
  @JvmField
  val activity: ActivityTestRule<NavigationActivity> = object : ActivityTestRule<NavigationActivity>(NavigationActivity::class.java) {

    override fun beforeActivityLaunched() {
      declare {
        single(name = HOST, override = true) { serverRule.host }
      }
      inject<SharedPreferences>().value.edit().clear().commit()
    }

    override fun afterActivityFinished() {
      inject<SharedPreferences>().value.edit().clear().commit()
    }
  }

  @Test
  @WebServer
  fun useAppContext() {

//    activity.launchActivity(null)
//    val appContext: App = ApplicationProvider.getApplicationContext()
    Thread.sleep(15000)
//    onWebView().withElement(findElement(Locator.ID, "password")).perform(webClick()) //   .perform(typeText("test"))
//    Thread.sleep(5000)
//    onWebView().withElement(findElement(Locator.NAME, "commit")).perform(webClick()) //   .perform(typeText("test"))
  }
}