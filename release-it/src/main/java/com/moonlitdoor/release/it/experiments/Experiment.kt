package com.moonlitdoor.release.it.experiments

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.moonlitdoor.shared.preference.live.data.SharedPreferenceBooleanLiveData
import org.koin.standalone.KoinComponent
import org.koin.standalone.get


data class Experiment(
    val key: String
) : KoinComponent {

  constructor(key: String, @StringRes titleId: Int = -1, @StringRes descriptionId: Int = -1) : this(key) {
    title = get<Context>().getString(titleId)
    description = get<Context>().getString(descriptionId)
  }

  var title: String? = null
  var description: String? = null
  val id = "com.moonlitdoor.release.it.experiment.$key"

  private val remoteConfig: FirebaseRemoteConfig = get()
  private val sharedPreferences: SharedPreferences = get()
  private val firebaseStatus = MutableLiveData<Boolean>()
  private val localStatus = SharedPreferenceBooleanLiveData(sharedPreferences, id, false)
  val firebase: Boolean = remoteConfig.getBoolean(key).also { firebaseStatus.setValue(it) }
  var local: Boolean = sharedPreferences.getBoolean(id, false) || firebase
    set(value) = sharedPreferences.edit().putBoolean(id, value).apply()

  val status = MediatorLiveData<Boolean>().also {
    it.addSource(localStatus) { value -> it.value = firebase || value }
    it.addSource(firebaseStatus) { value -> it.value = local || value }
  }

  fun isEnabled() = local || firebase

}
