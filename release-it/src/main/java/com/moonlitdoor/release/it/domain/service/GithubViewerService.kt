package com.moonlitdoor.release.it.domain.service

import android.content.Context
import android.content.Intent
import com.moonlitdoor.release.it.domain.dao.OwnerDao
import com.moonlitdoor.release.it.domain.entity.OwnerEntity
import com.moonlitdoor.release.it.domain.query.Repository
import com.moonlitdoor.release.it.domain.query.Viewer
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

class GithubViewerService : GithubService(GithubViewerService::class.java.name) {

  private val ownerDao: OwnerDao by inject()


  override fun onHandleIntent(intent: Intent?) {
    githubApi.queryViewer(Viewer.query()).execute().also { response ->
      when {
        !response.isSuccessful -> authDao.clearAuthToken()
        response.isSuccessful -> {
          response.body()?.data?.viewer?.let { viewer ->
            val ownerId = ownerDao.insert(OwnerEntity.from(viewer))
            processRepositories(viewer.repositories.nodes, ownerId, viewer.login)
            fetchViewerRepositories(ownerId, viewer.repositories.totalCount, viewer.repositories.nodes.size, viewer.repositories.pageInfo.endCursor)
          }
        }
      }
    }

  }

  private fun fetchViewerRepositories(ownerId: Long, totalCount: Int, count: Int, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryViewerRepositories(Repository.queryViewer(after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.viewer?.let { viewer ->
              processRepositories(viewer.repositories.nodes, ownerId, viewer.login)
              fetchViewerRepositories(ownerId, totalCount, count + viewer.repositories.nodes.size, viewer.repositories.pageInfo.endCursor)
            }
          }
        }
      }
    }.ignore()
  } else ignore()


  companion object {
    fun start(context: Context) = context.startService(Intent(context, GithubViewerService::class.java)).ignore()
  }
}
