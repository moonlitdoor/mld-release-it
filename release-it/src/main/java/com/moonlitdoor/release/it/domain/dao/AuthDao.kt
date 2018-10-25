package com.moonlitdoor.release.it.domain.dao

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.moonlitdoor.shared.preference.live.data.liveData

class AuthDao(private val preferences: SharedPreferences) {

  fun setAuthToken(authToken: String?) = preferences.edit().putString(AUTH_TOKEN, authToken).apply()

  fun getAuthToken(): String? = preferences.getString(AUTH_TOKEN, null)

  fun clearAuthToken() = preferences.edit().remove(AUTH_TOKEN)

  val authToken: LiveData<String?> = preferences.liveData(AUTH_TOKEN)

  companion object {
    private const val AUTH_TOKEN = "com.moonlitdoor.release.it.domain.dao.AuthDao.AUTH_TOKEN"
  }

}