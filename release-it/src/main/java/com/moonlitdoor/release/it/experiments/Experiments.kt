package com.moonlitdoor.release.it.experiments

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

object Experiments : KoinComponent {

  val experiments: List<Experiment<*>> = listOf()

  init {
    get<FirebaseRemoteConfig>().setDefaults(experiments.associateBy({ it.key }, { it.defaultValue }))
  }

}
