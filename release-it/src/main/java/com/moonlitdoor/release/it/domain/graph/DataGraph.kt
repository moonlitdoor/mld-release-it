package com.moonlitdoor.release.it.domain.graph

import com.moonlitdoor.release.it.GithubQuery

class DataGraph(
  val user: UserGraph
) {

  val repos = mutableListOf<RepoGraph>()

  companion object {
    fun from(viewer: GithubQuery.Viewer) = DataGraph(
      user = UserGraph.from(viewer)
    )
  }
}