package com.moonlitdoor.release.it.domain.query

class Viewer(
  val id: String,
  val login: String,
  val name: String?,
  val email: String,
  val createdAt: String,  //TODO DateTime
  val avatarUrl: String,  //TODO URI
  val repositories: Nodes<Repository>,
  val organizations: Nodes<Organization>
) {
  companion object {
    fun query(organizations: Int = QueryLimits.ORGANIZATION_NODES, repositories: Int = QueryLimits.REPOSITORY_NODES, releases: Int = QueryLimits.RELEASE_NODES) = ViewerQuery(
      """
        ${Organization.fragment(repositories, releases)}

        query {
          viewer {
            id
            login
            name
            email
            createdAt
            avatarUrl
            ${Nodes.query("repositories", repositories, "Repository")}
            ${Nodes.query("organizations", organizations, "Organization")}
          }
        }
      """.trimIndent()
    )
  }

  class Data(
    val viewer: Viewer
  )
}
