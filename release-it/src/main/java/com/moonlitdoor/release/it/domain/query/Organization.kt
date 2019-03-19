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

    fun query(organization: String, repositories: Int = QueryLimits.REPOSITORY_NODES, refs: Int = QueryLimits.REF_NODES,
              releases: Int = QueryLimits.RELEASE_NODES) =
        OrganizationQuery(
            """
            ${Repository.fragment(refs, releases)}

            query {
              organization(login: $organization)  {
                id
                login
                name
                email
                avatarUrl
                ${Nodes.field("repositories", repositories, "Repository")}
              }
            }
          """.trimIndent()
        )
  }

}