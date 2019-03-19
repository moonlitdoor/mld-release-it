package com.moonlitdoor.release.it.domain.query

class Repository(
    val name: String,
    val id: String,
    val description: String?,
    val isPrivate: Boolean,
    val viewerPermission: RepositoryPermission?,
    val refs: Nodes<Branch>,
    val releases: Nodes<Release>
) {
  companion object {
    fun queryViewer(repositories: Int = QueryLimits.REPOSITORY_NODES, refs: Int = QueryLimits.REF_NODES, releases: Int = QueryLimits.RELEASE_NODES,
                    after: String? = null) = RepositoryViewerQuery(
        """
        ${fragment(refs, releases)}

        query {
          viewer {
            login
            ${Nodes.field("repositories", repositories, "Repository", after)}
          }
        }
      """.trimIndent()
    )

    fun queryOrganization(repositories: Int = QueryLimits.REPOSITORY_NODES, refs: Int = QueryLimits.REF_NODES,
                          releases: Int = QueryLimits.RELEASE_NODES, login: String, after: String? = null) = RepositoryOrganizationQuery(
        """
        ${fragment(refs, releases)}

        query {
          organization(login: $login) {
            login
            ${Nodes.field("repositories", repositories, "Repository", after)}
          }
        }
      """.trimIndent()
    )

    fun fragment(refs: Int, releases: Int) =
        """
      ${Branch.fragment()}
      ${Release.fragment()}

      fragment Repository on Repository {
        name
        id
        description
        isPrivate
        viewerPermission
        ${Nodes.field("refs", refs, "Branch", refPrefix = "refs/heads/")}
        ${Nodes.field("releases", releases, "Release")}
      }
    """.trimIndent()

  }

}
