package com.moonlitdoor.release.it.domain.service

import android.content.Context
import android.content.Intent
import com.moonlitdoor.release.it.domain.dao.OwnerDao
import com.moonlitdoor.release.it.domain.entity.OwnerEntity
import com.moonlitdoor.release.it.domain.query.Organization
import com.moonlitdoor.release.it.domain.query.Repository
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

class GithubOrganizationService : GithubService(GithubOrganizationService::class.java.name) {

  private val ownerDao: OwnerDao by inject()

  override fun onHandleIntent(intent: Intent?) {

    intent?.extras?.getString(PARAMETER_ORGANIZATION)?.let { org ->
      githubApi.queryOrganization(Organization.query(org)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.organization?.let { organization ->
              val ownerId = ownerDao.insert(OwnerEntity.from(organization))
              processRepositories(organization.repositories.nodes, ownerId, organization.login)
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
              processRepositories(organization.repositories.nodes, userId, organization.login)
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
