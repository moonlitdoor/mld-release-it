package com.moonlitdoor.release.it.domain.service

import android.app.IntentService
import com.moonlitdoor.release.it.domain.api.GithubApi
import com.moonlitdoor.release.it.domain.dao.AuthDao
import com.moonlitdoor.release.it.domain.dao.BranchDao
import com.moonlitdoor.release.it.domain.dao.ReleaseDao
import com.moonlitdoor.release.it.domain.dao.RepositoryDao
import com.moonlitdoor.release.it.domain.entity.BranchEntity
import com.moonlitdoor.release.it.domain.entity.ReleaseEntity
import com.moonlitdoor.release.it.domain.entity.RepositoryEntity
import com.moonlitdoor.release.it.domain.query.Branch
import com.moonlitdoor.release.it.domain.query.Release
import com.moonlitdoor.release.it.domain.query.Repository
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.ext.android.inject

abstract class GithubService(name: String) : IntentService(name) {

  protected val githubApi: GithubApi by inject()
  protected val authDao: AuthDao by inject()

  private val branchDao: BranchDao by inject()
  private val releaseDao: ReleaseDao by inject()
  private val repositoryDao: RepositoryDao by inject()

  protected fun processRepositories(nodes: List<Repository>, ownerId: Long, login: String) {
    nodes.forEach { repository ->
      val repositoryId = repositoryDao.insert(RepositoryEntity.from(ownerId, login, repository))
      repository.refs.nodes.forEach { branch ->
        branchDao.insert(BranchEntity.from(repositoryId, branch))
      }
      repository.releases.nodes.forEach { release ->
        releaseDao.insert(ReleaseEntity.from(repositoryId, release))
      }
      fetchBranches(repositoryId, repository.refs.totalCount, repository.refs.nodes.size, login, repository.name,
          repository.refs.pageInfo.endCursor)
      fetchReleases(repositoryId, repository.releases.totalCount, repository.releases.nodes.size, login, repository.name,
          repository.releases.pageInfo.endCursor)
    }
  }

  private fun fetchBranches(repositoryId: Long, totalCount: Int, count: Int, owner: String, repo: String,
                              endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let { after ->
      githubApi.queryBranches(Branch.query(owner = owner, name = repo, after = after)).execute().also { response ->
        when {
          !response.isSuccessful -> authDao.clearAuthToken()
          response.isSuccessful -> {
            response.body()?.data?.repository?.let { repository ->
              repository.refs.nodes.forEach { branch ->
                branchDao.insert(BranchEntity.from(repositoryId, branch))
              }
              fetchReleases(repositoryId, totalCount, count + repository.refs.nodes.size, owner, repo, repository.refs.pageInfo.endCursor)
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

}