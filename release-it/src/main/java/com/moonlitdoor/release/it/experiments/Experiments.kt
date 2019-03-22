package com.moonlitdoor.release.it.experiments

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.standalone.KoinComponent
import org.koin.standalone.get

object Experiments : KoinComponent {

  enum class ABC {
    A, B, C
  }

  val TEST3 = Experiment("exp_test_3", ABC::class.java, ABC.A)
  val TEST2 = Experiment("exp_test_2")

  val experiments: List<Experiment<*>> = listOf(
      TEST3,
      TEST2
  )

  init {
    get<FirebaseRemoteConfig>().setDefaults(experiments.associateBy({ it.key }, { it.defaultValue }))
  }

}
