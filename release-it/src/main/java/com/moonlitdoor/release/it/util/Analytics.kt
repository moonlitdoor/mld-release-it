package com.moonlitdoor.release.it.util

import android.content.SharedPreferences
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.moonlitdoor.release.it.BuildConfig
import com.moonlitdoor.release.it.settings.SettingsFragment
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import java.util.*


object Analytics : KoinComponent {

  private val firebase = FirebaseAnalytics.getInstance(get()).also {
    it.setUserId(get<SharedPreferences>().run {
      getString(USER_ID_KEY, null) ?: UUID.randomUUID().toString().also { id ->
        edit().putString(USER_ID_KEY, id).apply()
      }
    })
    it.setUserProperty(USER_PROPERTY_VERSION_CODE, BuildConfig.VERSION_CODE.toString())
    it.setUserProperty(USER_PROPERTY_EXPERIMENTS_ENABLED, get<SharedPreferences>().getBoolean(SettingsFragment.EXPERIMENTS_ENABLED, false).toString())
  }

  private const val USER_ID_KEY = "com.moonlitdoor.release.it.Analytics.USER_ID"
  private const val USER_PROPERTY_VERSION_CODE = "version_code"
  private const val USER_PROPERTY_EXPERIMENTS_ENABLED = "experiments_enabled"

  private const val EVENT_SPLASH_ACTIVITY_TIMER = "splash_timer"
  private const val PARAM_SPLASH_ACTIVITY_TIMER_LENGTH = "length"

  private const val EVENT_EXPERIMENTS_ENABLED = "experiments_enabled"
  private const val PARAM_EXPERIMENTS_ENABLED_LOCATION = "location"
  private const val PARAM_VALUE_EXPERIMENTS_ENABLED_SETTINGS_VERSION = "settings_version"

  fun splashActivityTimer(start: Long, end: Long) {
    firebase.logEvent(EVENT_SPLASH_ACTIVITY_TIMER, Bundle().also {
      it.putLong(PARAM_SPLASH_ACTIVITY_TIMER_LENGTH, end - start)
    })
  }

  fun experimentsEnabledFromSettings() {
    firebase.logEvent(EVENT_EXPERIMENTS_ENABLED, Bundle().also {
      it.putString(PARAM_EXPERIMENTS_ENABLED_LOCATION, PARAM_VALUE_EXPERIMENTS_ENABLED_SETTINGS_VERSION)
    })
    firebase.setUserProperty(USER_PROPERTY_EXPERIMENTS_ENABLED,
        get<SharedPreferences>().getBoolean(SettingsFragment.EXPERIMENTS_ENABLED, true).toString())
  }
}