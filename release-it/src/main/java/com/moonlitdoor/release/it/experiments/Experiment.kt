package com.moonlitdoor.release.it.experiments

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.standalone.KoinComponent
import org.koin.standalone.get


data class Experiment<T : Enum<T>>(val key: String, val c: Class<T>, val defaultValue: T) : KoinComponent {

  private val remoteConfig: FirebaseRemoteConfig = get()
  private val sharedPreferences: SharedPreferences = get()

  enum class BOOLEAN {
    FALSE, TRUE
  }

  constructor(key: String, c: Class<T>, defaultValue: T, title: String? = null, description: String? = null) : this(key, c, defaultValue) {
    this.title = title
    this.description = description
  }

  constructor(key: String, c: Class<T>, defaultValue: T, @StringRes title: Int = 0, @StringRes description: Int = 0) : this(key, c, defaultValue) {
    this.title = get<Context>().getString(title)
    this.description = get<Context>().getString(description)
  }

  val id = "com.moonlitdoor.release.it.experiment.$key"

  var title: String? = null
  var description: String? = null
  val firebase: String = (remoteConfig.getString(key).toUpperCase()).toUpperCase()
  var local: String = sharedPreferences.getString(id, REMOTE) ?: REMOTE
    set(value) {
      field = value
      sharedPreferences.edit().putString(id, value).apply()
    }

  val options: List<String> = listOf(REMOTE) + c.enumConstants.map { it.name.toUpperCase() }

  fun getOption(): T = c.enumConstants.asList().find { if (local == REMOTE) firebase == it.name else local == it.name } ?: defaultValue

  companion object {
    private const val REMOTE = "REMOTE"
    operator fun invoke(key: String) = Experiment(key, BOOLEAN::class.java, BOOLEAN.FALSE)
    operator fun invoke(key: String, title: String? = null, description: String? = null) = Experiment(key, BOOLEAN::class.java, BOOLEAN.FALSE, title,
        description)

    operator fun invoke(key: String, @StringRes title: Int = 0, @StringRes description: Int = 0) = Experiment(key, BOOLEAN::class.java, BOOLEAN.FALSE,
        title, description)
  }

}
