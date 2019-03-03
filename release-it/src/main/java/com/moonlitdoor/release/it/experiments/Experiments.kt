package com.moonlitdoor.release.it.experiments

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

object Experiments : KoinComponent {


  val TEST1 = Experiment("exp_test_1")
  val TEST2 = Experiment("exp_test_2")

  val experiments: List<Experiment> = listOf(
      TEST1,
      TEST2
  )

  init {
    get<FirebaseRemoteConfig>().setDefaults(experiments.associateBy({ it.key }, { it.local }))
  }

}
