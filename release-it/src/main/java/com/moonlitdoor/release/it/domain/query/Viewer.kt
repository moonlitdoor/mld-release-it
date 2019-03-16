package com.moonlitdoor.release.it.domain.query

import android.net.Uri

class Viewer(
    val id: String,
    val login: String,
    val name: String?,
    val email: String,
    val avatarUrl: Uri,
    val repositories: Nodes<Repository>
) {
  companion object {
    fun query(repositories: Int = QueryLimits.REPOSITORY_NODES, releases: Int = QueryLimits.RELEASE_NODES) = ViewerQuery(
      """
        ${Repository.fragment(releases)}

        query {
          viewer {
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
    val viewer: Viewer
  )
}
