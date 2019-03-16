package com.moonlitdoor.release.it.domain.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.moonlitdoor.release.it.domain.api.GithubApi
import com.moonlitdoor.release.it.domain.dao.AuthDao
import com.moonlitdoor.release.it.domain.dao.OwnerDao
import com.moonlitdoor.release.it.domain.dao.ReleaseDao
import com.moonlitdoor.release.it.domain.dao.RepositoryDao
import com.moonlitdoor.release.it.domain.entity.OwnerEntity
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.query.Release
import com.moonlitdoor.release.it.domain.query.Repository
import com.moonlitdoor.release.it.domain.query.Viewer
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

class GithubViewerService : IntentService(GithubViewerService::class.java.name) {

  private val authDao: AuthDao by inject()
  private val ownerDao: OwnerDao by inject()
  private val repositoryDao: RepositoryDao by inject()
  private val releaseDao: ReleaseDao by inject()
  private val githubApi: GithubApi by inject()

  override fun onHandleIntent(intent: Intent?) {
    githubApi.queryViewer(Viewer.query()).execute().also { response ->
      when {
        !response.isSuccessful -> authDao.clearAuthToken()
        response.isSuccessful -> {
          response.body()?.data?.viewer?.let { viewer ->
            val ownerId = ownerDao.insert(OwnerEntity.from(viewer))
            viewer.repositories.nodes
                .forEach { repository ->
                  val repositoryId = repositoryDao.insert(RepositoryEntity.from(ownerId, viewer.login, repository))
                  repository.releases.nodes.forEach { release ->
                    releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                  }
                  fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, viewer.login, repository.name,
                      repository.releases.pageInfo.endCursor)
                }
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
              viewer.repositories.nodes
                  .forEach { repository ->
                    val repositoryId = repositoryDao.insert(RepositoryEntity.from(ownerId, viewer.login, repository))
                    repository.releases.nodes.forEach { release ->
                      releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                    }
                    fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, viewer.login, repository.name,
                        repository.releases.pageInfo.endCursor)
                  }
              fetchViewerRepositories(ownerId, totalCount, count + viewer.repositories.nodes.size, viewer.repositories.pageInfo.endCursor)
            }
          }
        }
      }
    }.ignore()
  } else ignore()

  private fun fetchReleases(repositoryId: Long, totalCount: Int, count: Int, owner: String, repo: String, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryReleases(Release.query(owner = owner, name = repo, after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.repository?.let { repository ->
              repository.releases.nodes.forEach { release ->
                releaseDao.insert(ReleaseEntity.from(repositoryId, release))
              }
              fetchReleases(repositoryId, totalCount, count + repository.releases.nodes.size, owner, repo, repository.releases.pageInfo.endCursor)
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
