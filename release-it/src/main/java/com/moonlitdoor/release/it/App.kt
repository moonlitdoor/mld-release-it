package com.moonlitdoor.release.it

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.moonlitdoor.release.it.experiments.Experiment
import com.moonlitdoor.release.it.experiments.Experiments
import org.koin.android.ext.android.startKoin

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    startKoin(this, listOf(di))


    when(Experiments.TEST2.getOption()) {
      Experiment.BOOLEAN.TRUE-> {}
//      Experiment.BOOLEAN.FALSE ->{}
    }

    when(Experiments.TEST3.getOption()){
      Experiments.ABC.A ->{}
    }

  }
}