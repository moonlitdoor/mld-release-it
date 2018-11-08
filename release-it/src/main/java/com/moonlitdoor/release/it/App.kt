package com.moonlitdoor.release.it

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.startKoin

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    startKoin(this, listOf(di))
  }
}