package com.moonlitdoor.release.it.domain.query

class RepositoryViewerData(
    val viewer: Viewer
) {
  class Viewer(
      val login: String,
      val repositories: Nodes<Repository>
  )
}