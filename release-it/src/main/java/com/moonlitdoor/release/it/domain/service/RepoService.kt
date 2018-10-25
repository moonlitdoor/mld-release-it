package com.moonlitdoor.release.it.domain.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.moonlitdoor.release.it.domain.api.RepoApi
import com.moonlitdoor.release.it.domain.api.UserApi
import com.moonlitdoor.release.it.domain.dao.AuthDao
import com.moonlitdoor.release.it.domain.dao.RepoDao
import com.moonlitdoor.release.it.domain.dao.UserDao
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject


class RepoService : IntentService("RepoService") {

  private val authDao: AuthDao by inject()
  private val userApi: UserApi by inject()
  private val userDao: UserDao by inject()
  private val repoApi: RepoApi by inject()
  private val repoDao: RepoDao by inject()

  override fun onHandleIntent(intent: Intent?) {
    userApi.getUser().execute().apply {
      when {
        !isSuccessful -> authDao.clearAuthToken()
        isSuccessful -> userDao
      }
    }


//    repoApi.getRepos().execute().apply {
//      when {
//        !isSuccessful -> authDao.clearAuthToken()
//        isSuccessful -> {
//          repoDao.insert(
//            body()
//              ?.asSequence()
//              ?.filter { it.permissions.admin || it.permissions.push }
//              ?.map { RepoEntity.from(it) }
//              ?.toList()
//          )
//        }
//      }
//    }
  }

  companion object {

    private const val EXTRA_PARAM1 = "com.moonlitdoor.release.it.domain.service.RepoService.PARAM1"
    private const val EXTRA_PARAM2 = "com.moonlitdoor.release.it.domain.service.extra.PARAM2"

    fun start(context: Context) = context.startService(Intent(context, RepoService::class.java).apply {
      putExtra(EXTRA_PARAM1, "")
      putExtra(EXTRA_PARAM2, "")
    }).ignore()
  }
}
