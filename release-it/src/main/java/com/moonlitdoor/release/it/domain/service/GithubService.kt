package com.moonlitdoor.release.it.domain.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.moonlitdoor.release.it.*
import com.moonlitdoor.release.it.domain.dao.AuthDao
import com.moonlitdoor.release.it.domain.dao.ServiceDao
import com.moonlitdoor.release.it.domain.graph.DataGraph
import com.moonlitdoor.release.it.domain.graph.ReleaseGraph
import com.moonlitdoor.release.it.domain.graph.RepoGraph
import com.moonlitdoor.release.it.domain.query.*
import com.moonlitdoor.release.it.extension.ignore
import com.moonlitdoor.release.it.type.RepositoryPermission
import org.koin.android.ext.android.inject


class GithubService : IntentService("GithubService") {

  private val authDao: AuthDao by inject()
  private val githubQueryProvider: GithubQueryProvider by inject()
  private val organizationsAfterQueryProvider: OrganizationsAfterQueryProvider by inject()
  private val organizationsRepositoriesAfterQueryProvider: OrganizationsRepositoriesAfterQueryProvider by inject()
  private val releasesAfterQueryProvider: ReleasesAfterQueryProvider by inject()
  private val viewerRepositoriesAfterQueryProvider: ViewerRepositoriesAfterQueryProvider by inject()
  private val serviceDao: ServiceDao by inject()

  override fun onHandleIntent(intent: Intent?) {
    githubQueryProvider.get().enqueue(
      object : ApolloCall.Callback<GithubQuery.Data>() {
        override fun onResponse(response: Response<GithubQuery.Data>) {
          response.data()?.viewer()?.let { viewer ->
            DataGraph.from(viewer).also {
              viewer.repositories().nodes()?.let { repositories ->
                repositories.filter { repo -> filter(repo.viewerPermission()) }.forEach { repo ->
                  it.repos.add(RepoGraph.from(viewer.login(), repo).apply {
                    repo.releases().nodes()?.let { rs ->
                      rs.forEach { node ->
                        releases.add(ReleaseGraph.from(node))
                      }
                      fetchReleases(releases, repo.releases().totalCount(), rs.size, viewer.login(), repo.name(), repo.releases().pageInfo().endCursor())
                    }
                  })
                }
                fetchViewerRepositories(it, viewer.repositories().totalCount(), repositories.size, viewer.repositories().pageInfo().endCursor())
              }
              viewer.organizations().nodes()?.let { organizations ->
                organizations.forEach { org ->
                  org.repositories().nodes()?.let { repositories ->
                    repositories.filter { repo -> filter(repo.viewerPermission()) }.forEach { repo ->
                      it.repos.add(RepoGraph.from(org.login(), repo).apply {
                        repo.releases().nodes()?.let { rs ->
                          rs.forEach { node ->
                            releases.add(ReleaseGraph.from(node))
                          }
                          fetchReleases(releases, repo.releases().totalCount(), rs.size, viewer.login(), repo.name(), repo.releases().pageInfo().endCursor())
                        }
                      })
                    }
                    fetchOrganizationRepositories(it, org.repositories().totalCount(), repositories.size, org.login(), org.repositories().pageInfo().endCursor())
                  }
                }
                fetchOrganizations(it, viewer.organizations().totalCount(), organizations.size, viewer.organizations().pageInfo().endCursor())
              }
              serviceDao.insert(it)
            }
          }
        }

        override fun onFailure(e: ApolloException) {
          authDao.clearAuthToken()
        }
      }
    )
  }

  private fun fetchViewerRepositories(data: DataGraph, totalCount: Int, count: Int, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let {
      viewerRepositoriesAfterQueryProvider.get(it).enqueue(
        object : ApolloCall.Callback<ViewerRepositoriesAfterQuery.Data>() {
          override fun onResponse(response: Response<ViewerRepositoriesAfterQuery.Data>) {
            response.data()?.viewer()?.let { viewer ->
              viewer.repositories().nodes()?.let { repositories ->
                repositories.filter { repo -> filter(repo.viewerPermission()) }.forEach { repo ->
                  data.repos.add(RepoGraph.from(viewer.login(), repo).apply {
                    repo.releases().nodes()?.let { rs ->
                      rs.forEach { node ->
                        releases.add(ReleaseGraph.from(node))
                      }
                      fetchReleases(releases, repo.releases().totalCount(), rs.size, viewer.login(), repo.name(), repo.releases().pageInfo().endCursor())
                    }
                  })
                }
                fetchViewerRepositories(data, totalCount, count + repositories.size, viewer.repositories().pageInfo().endCursor())
              }
            }
          }

          override fun onFailure(e: ApolloException) {
            authDao.clearAuthToken()
          }
        }
      )
    }.ignore()
  } else ignore()

  private fun fetchOrganizations(data: DataGraph, totalCount: Int, count: Int, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let {
      organizationsAfterQueryProvider.get(it).enqueue(
        object : ApolloCall.Callback<OrganizationsAfterQuery.Data>() {
          override fun onResponse(response: Response<OrganizationsAfterQuery.Data>) {
            response.data()?.viewer()?.let { viewer ->
              viewer.organizations().nodes()?.let { organizations ->
                organizations.forEach { org ->
                  org.repositories().nodes()?.let { repositories ->
                    repositories.filter { repo -> filter(repo.viewerPermission()) }.forEach { repo ->
                      data.repos.add(RepoGraph.from(org.login(), repo).apply {
                        repo.releases().nodes()?.let { rs ->
                          rs.forEach { node ->
                            releases.add(ReleaseGraph.from(node))
                          }
                          fetchReleases(releases, repo.releases().totalCount(), rs.size, org.login(), repo.name(), repo.releases().pageInfo().endCursor())
                        }
                      })
                    }
                    fetchOrganizationRepositories(data, org.repositories().totalCount(), repositories.size, org.login(), org.repositories().pageInfo().endCursor())
                  }
                }
                fetchOrganizations(data, viewer.organizations().totalCount(), organizations.size, viewer.organizations().pageInfo().endCursor())
              }
            }
          }

          override fun onFailure(e: ApolloException) {
            authDao.clearAuthToken()
          }
        }
      )
    }.ignore()
  } else ignore()

  private fun fetchOrganizationRepositories(data: DataGraph, totalCount: Int, count: Int, organization: String, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let {
      organizationsRepositoriesAfterQueryProvider.get(organization, it).enqueue(
        object : ApolloCall.Callback<OrganizationRepositoriesAfterQuery.Data>() {
          override fun onResponse(response: Response<OrganizationRepositoriesAfterQuery.Data>) {
            response.data()?.organization()?.let { org ->
              org.repositories().nodes()?.let { repos ->
                repos.filter { repo -> filter(repo.viewerPermission()) }.forEach { repo ->
                  data.repos.add(RepoGraph.from(org.login(), repo).apply {
                    repo.releases().nodes()?.let { rs ->
                      rs.forEach { node ->
                        releases.add(ReleaseGraph.from(node))
                      }
                      fetchReleases(releases, repo.releases().totalCount(), rs.size, org.login(), repo.name(), repo.releases().pageInfo().endCursor())
                    }
                  })
                }
                fetchOrganizationRepositories(data, totalCount, repos.size + count, organization, org.repositories().pageInfo().endCursor())
              }
            }
          }

          override fun onFailure(e: ApolloException) {
            authDao.clearAuthToken()
          }
        }
      )
    }.ignore()
  } else ignore()

  private fun fetchReleases(data: MutableList<ReleaseGraph>, totalCount: Int, count: Int, owner: String, repo: String, endCursor: String?): Unit = if (totalCount > count) {
    endCursor?.let {
      releasesAfterQueryProvider.get(owner, repo, it).enqueue(
        object : ApolloCall.Callback<ReleasesAfterQuery.Data>() {
          override fun onResponse(response: Response<ReleasesAfterQuery.Data>) {
            response.data()?.repository()?.releases()?.let { releases ->
              releases.nodes()?.let { rs ->
                rs.forEach { release ->
                  data.add(ReleaseGraph.from(release))
                }
                fetchReleases(data, totalCount, rs.size + count, owner, repo, releases.pageInfo().endCursor())
              }
            }
          }

          override fun onFailure(e: ApolloException) {
            authDao.clearAuthToken()
          }
        }
      )
    }.ignore()
  } else ignore()

  private fun filter(permission: RepositoryPermission?) = permission == RepositoryPermission.ADMIN || permission == RepositoryPermission.WRITE

  companion object {
    fun start(context: Context) = context.startService(Intent(context, GithubService::class.java)).ignore()
  }
}
