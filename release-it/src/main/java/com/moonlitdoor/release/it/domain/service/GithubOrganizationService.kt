package com.moonlitdoor.release.it.domain.service

import android.content.Context
import android.content.Intent
import com.moonlitdoor.release.it.domain.dao.OwnerDao
import com.moonlitdoor.release.it.domain.dao.RepositoryDao
import com.moonlitdoor.release.it.domain.entity.BranchEntity
import com.moonlitdoor.release.it.domain.entity.OwnerEntity
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.query.Organization
import com.moonlitdoor.release.it.domain.query.Repository
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

class GithubOrganizationService : GithubService(GithubOrganizationService::class.java.name) {

  private val ownerDao: OwnerDao by inject()
  private val repositoryDao: RepositoryDao by inject()

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
                    repository.refs.nodes.forEach { branch ->
                      branchDao.insert(BranchEntity.from(repositoryId, branch))
                    }
                    repository.releases.nodes.forEach { release ->
                      releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                    }
                    fetchBranches(repositoryId, repository.refs.totalCount, repository.refs.nodes.size, organization.login, repository.name,
                        repository.refs.pageInfo.endCursor)
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
                    repository.refs.nodes.forEach { branch ->
                      branchDao.insert(BranchEntity.from(repositoryId, branch))
                    }
                    repository.releases.nodes.forEach { release ->
                      releaseDao.insert(ReleaseEntity.from(repositoryId, release))
                    }
                    fetchBranches(repositoryId, repository.refs.totalCount, repository.refs.nodes.size, organization.login, repository.name,
                        repository.refs.pageInfo.endCursor)
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

  companion object {

    private const val PARAMETER_ORGANIZATION = "com.moonlitdoor.release.it.GithubOrganizationService.ORGANIZATION"

    fun start(context: Context, organization: String) = context.startService(Intent(context, GithubOrganizationService::class.java).also {
      it.putExtra(PARAMETER_ORGANIZATION, organization)
    }).ignore()
  }
}
