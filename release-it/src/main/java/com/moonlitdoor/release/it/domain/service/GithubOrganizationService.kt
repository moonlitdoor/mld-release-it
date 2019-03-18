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
import com.moonlitdoor.release.it.domain.query.Organization
import com.moonlitdoor.release.it.domain.query.Release
import com.moonlitdoor.release.it.domain.query.Repository
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

class GithubOrganizationService : IntentService(GithubOrganizationService::class.java.name) {

  private val authDao: AuthDao by inject()
  private val ownerDao: OwnerDao by inject()
  private val repositoryDao: RepositoryDao by inject()
  private val releaseDao: ReleaseDao by inject()
  private val githubApi: GithubApi by inject()

  override fun onHandleIntent(intent: Intent?) {

    intent?.extras?.getString(PARAMETER_ORGANIZATION)?.let { org ->
      githubApi.queryOrganization(Organization.query(org)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.organization?.let { organization ->
              val ownerId = ownerDao.insert(OwnerEntity.from(organization))
              organization.repositories.nodes
                  .forEach { repository ->
                    val repositoryId = repositoryDao.insert(RepositoryEntity.from(ownerId, organization.login, repository))
                    repository.releases.nodes.forEach { release ->
                      releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                    }
                    fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, organization.login, repository.name,
                        repository.releases.pageInfo.endCursor)
                  }
              fetchOrganizationRepositories(ownerId, organization.repositories.totalCount, organization.repositories.nodes.size, organization.login,
                  organization.repositories.pageInfo.endCursor)
            }
          }
        }
      }
    }
  }

  private fun fetchOrganizationRepositories(userId: Long, totalCount: Int, count: Int, login: String,
                                            endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryOrganizationRepositories(Repository.queryOrganization(login = login, after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.organization?.let { organization ->
              organization.repositories.nodes
                  .forEach { repository ->
                    val repositoryId = repositoryDao.insert(RepositoryEntity.from(userId, organization.login, repository))
                    repository.releases.nodes.forEach { release ->
                      releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                    }
                    fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, organization.login, repository.name,
                        repository.releases.pageInfo.endCursor)
                  }
              fetchOrganizationRepositories(userId, totalCount, count + organization.repositories.nodes.size, login,
                  organization.repositories.pageInfo.endCursor)
            }
          }
        }
      }
    }.ignore()
  } else ignore()

  private fun fetchReleases(repositoryId: Long, totalCount: Int, count: Int, owner: String, repo: String,
                            endCursor: String?): Unit = if (totalCount > count) {
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

    private const val PARAMETER_ORGANIZATION = "com.moonlitdoor.release.it.GithubOrganizationService.ORGANIZATION"

    fun start(context: Context, organization: String) = context.startService(Intent(context, GithubOrganizationService::class.java).also {
      it.putExtra(PARAMETER_ORGANIZATION, organization)
    }).ignore()
  }
}
