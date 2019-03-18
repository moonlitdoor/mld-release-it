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

    fun query(organization: String, repositories: Int = QueryLimits.REPOSITORY_NODES, releases: Int = QueryLimits.RELEASE_NODES) = OrganizationQuery(
        """
          ${Repository.fragment(releases)}

          query {
            organization(login: $organization)  {
              id
              login
              name
              email
              avatarUrl
              ${Nodes.query("repositories", repositories, "Repository")}
            }
          }
        """.trimIndent()
    )
  }

  class Data(
      val organization: Organization
  )

}