package com.moonlitdoor.release.it.domain.query

class Repository(
    val name: String,
    val id: String,
    val description: String?,
    val isPrivate: Boolean,
    val viewerPermission: RepositoryPermission?,
    val releases: Nodes<Release>
) {
  companion object {
    fun queryViewer(repositories: Int = QueryLimits.REPOSITORY_NODES, releases: Int = QueryLimits.RELEASE_NODES, after: String? = null) = ViewerRepositoryQuery(
      """
        ${fragment(releases)}

        query {
          viewer {
            login
            ${Nodes.query("repositories", repositories, "Repository", after)}
          }
        }
      """.trimIndent()
    )

    fun queryOrganization(repositories: Int = QueryLimits.REPOSITORY_NODES, releases: Int = QueryLimits.RELEASE_NODES, login: String, after: String? = null) = OrganizationRepositoryQuery(
      """
        ${fragment(releases)}

        query {
          organization(login: $login) {
            login
            ${Nodes.query("repositories", repositories, "Repository", after)}
          }
        }
      """.trimIndent()
    )

    fun fragment(releases: Int) = """
      ${Release.fragment()}

      fragment Repository on Repository {
        name
        id
        description
        isPrivate
        viewerPermission
        ${Nodes.query("releases", releases, "Release")}
      }
    """.trimIndent()

  }

  class ViewerData(
    val viewer: Viewer
  ) {
    class Viewer(
      val login: String,
      val repositories: Nodes<Repository>
    )
  }

  class OrganizationData(
    val organization: Organization
  ) {
    class Organization(
      val login: String,
      val repositories: Nodes<Repository>
    )
  }
}
