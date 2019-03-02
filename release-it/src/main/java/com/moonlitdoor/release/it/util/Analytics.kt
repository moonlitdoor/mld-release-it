package com.moonlitdoor.release.it.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.standalone.KoinComponent
import org.koin.standalone.get


object Analytics : KoinComponent {

  private val firebase = FirebaseAnalytics.getInstance(get())

  private const val EVENT_SPLASH_ACTIVITY_TIMER = "splash_timer"
  private const val PARAM_SPLASH_ACTIVITY_TIMER_LENGTH = "length"

  fun splashActivityTimer(start: Long, end: Long) {
    firebase.logEvent(EVENT_SPLASH_ACTIVITY_TIMER, Bundle().also {
      it.putLong(PARAM_SPLASH_ACTIVITY_TIMER_LENGTH, end - start)
    })
  }
}