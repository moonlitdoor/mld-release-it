package com.moonlitdoor.release.it.domain.query

import android.net.Uri

class Organization(
    val id: String,
    val login: String,
    val name: String?,
    val email: String,
    val avatarUrl: Uri,
    val repositories: Nodes<Repository>
) {
  companion object {

    fun query(organizations: Int = QueryLimits.ORGANIZATION_NODES, repositories: Int = QueryLimits.REPOSITORY_NODES, releases: Int = QueryLimits.RELEASE_NODES, after: String? = null) = OrganizationQuery(
      """
        ${fragment(repositories, releases)}

        query {
          viewer {
            ${Nodes.query("organizations", organizations, "Organization", after)}
          }
        }
      """.trimIndent()
    )

    fun fragment(repositories: Int, releases: Int) = """
        ${Repository.fragment(releases)}

        fragment Organization on Organization {
          id
          login
          name
          email
          avatarUrl
          ${Nodes.query("repositories", repositories, "Repository")}
        }
      """.trimIndent()
  }

  class Data(
      val organization: Organization
  )

}