package com.moonlitdoor.release.it.domain.query

class Organization(
  val name: String?,
  val login: String,
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
          name
          login
          ${Nodes.query("repositories", repositories, "Repository")}
        }
      """.trimIndent()
  }

  class Data(
    val viewer: Viewer
  ) {
    class Viewer(
      val organizations: Nodes<Organization>
    )
  }

}