package com.moonlitdoor.release.it.domain.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.moonlitdoor.release.it.domain.api.GithubApi
import com.moonlitdoor.release.it.domain.dao.AuthDao
import com.moonlitdoor.release.it.domain.dao.ReleaseDao
import com.moonlitdoor.release.it.domain.dao.RepositoryDao
import com.moonlitdoor.release.it.domain.dao.UserDao
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.entity.UserEntity
import com.moonlitdoor.release.it.domain.query.*
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

class GithubService : IntentService("GithubService") {

  private val authDao: AuthDao by inject()
  private val userDao: UserDao by inject()
  private val repositoryDao: RepositoryDao by inject()
  private val releaseDao: ReleaseDao by inject()
  private val githubApi: GithubApi by inject()

  private val LOG_TAG = "GithubService"

  override fun onHandleIntent(intent: Intent?) {
    Log.i("OkHttp", "currentTime=${System.currentTimeMillis()}")
    githubApi.queryViewer(Viewer.query()).execute().also { response ->
      when {
        !response.isSuccessful -> authDao.clearAuthToken()
        response.isSuccessful -> {
          response.body()?.data?.viewer?.let { viewer ->
            val userId = userDao.insert(UserEntity.from(viewer))
            Log.i(LOG_TAG, "1 ")
            viewer.repositories.nodes
              .filter { filter(it.viewerPermission) }
              .forEach { repository ->
                val repositoryId = repositoryDao.insert(RepositoryEntity.from(userId, viewer.login, repository))
                repository.releases.nodes.forEach { release ->
                  releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                }
                fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, viewer.login, repository.name, repository.releases.pageInfo.endCursor)
              }
            fetchViewerRepositories(userId, viewer.repositories.totalCount, viewer.repositories.nodes.size, viewer.repositories.pageInfo.endCursor)
            viewer.organizations.nodes.forEach { organization ->
              organization.repositories.nodes
                .filter { filter(it.viewerPermission) }
                .forEach { repository ->
                  val repositoryId = repositoryDao.insert(RepositoryEntity.from(userId, organization.login, repository))
                  repository.releases.nodes.forEach { release ->
                    releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                  }
                  fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, organization.login, repository.name, repository.releases.pageInfo.endCursor)
                }
              fetchOrganizationRepositories(userId, organization.repositories.totalCount, organization.repositories.nodes.size, organization.login, organization.repositories.pageInfo.endCursor)
            }
            fetchOrganizations(userId, viewer.organizations.totalCount, viewer.organizations.nodes.size, viewer.organizations.pageInfo.endCursor)
          }
        }
      }
    }

  }

  private fun fetchViewerRepositories(userId: Long, totalCount: Int, count: Int, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryViewerRepositories(Repository.queryViewer(after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.viewer?.let { viewer ->
              viewer.repositories.nodes
                .filter { filter(it.viewerPermission) }
                .forEach { repository ->
                  val repositoryId = repositoryDao.insert(RepositoryEntity.from(userId, viewer.login, repository))
                  repository.releases.nodes.forEach { release ->
                    releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                  }
                  fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, viewer.login, repository.name, repository.releases.pageInfo.endCursor)
                }
              fetchViewerRepositories(userId, totalCount, count + viewer.repositories.nodes.size, viewer.repositories.pageInfo.endCursor)
            }
          }
        }
      }
    }.ignore()
  } else ignore()

  private fun fetchOrganizations(userId: Long, totalCount: Int, count: Int, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryViewerOrganizations(Organization.query(after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.viewer?.let { viewer ->
              viewer.organizations.nodes.forEach { organization ->
                organization.repositories.nodes
                  .filter { filter(it.viewerPermission) }
                  .forEach { repository ->
                    val repositoryId = repositoryDao.insert(RepositoryEntity.from(userId, organization.login, repository))
                    repository.releases.nodes.forEach { release ->
                      releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                    }
                    fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, organization.login, repository.name, repository.releases.pageInfo.endCursor)
                  }
                fetchOrganizationRepositories(userId, organization.repositories.totalCount, organization.repositories.nodes.size, organization.login, organization.repositories.pageInfo.endCursor)
              }
              fetchOrganizations(userId, totalCount, count + viewer.organizations.nodes.size, viewer.organizations.pageInfo.endCursor)
            }
          }
        }
      }
    }.ignore()
  } else ignore()

  private fun fetchOrganizationRepositories(userId: Long, totalCount: Int, count: Int, login: String, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryOrganizationRepositories(Repository.queryOrganization(login = login, after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.organization?.let { organization ->
              organization.repositories.nodes
                .filter { filter(it.viewerPermission) }
                .forEach { repository ->
                  val repositoryId = repositoryDao.insert(RepositoryEntity.from(userId, organization.login, repository))
                  repository.releases.nodes.forEach { release ->
                    releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                  }
                  fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, organization.login, repository.name, repository.releases.pageInfo.endCursor)
                }
              fetchOrganizationRepositories(userId, totalCount, count + organization.repositories.nodes.size, login, organization.repositories.pageInfo.endCursor)
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

  private fun filter(permission: RepositoryPermission?) = permission == RepositoryPermission.ADMIN || permission == RepositoryPermission.WRITE

  companion object {
    fun start(context: Context) = context.startService(Intent(context, GithubService::class.java)).ignore()
  }
}
