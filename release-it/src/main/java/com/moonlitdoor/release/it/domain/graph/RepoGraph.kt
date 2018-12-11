package com.moonlitdoor.release.it.domain.graph

class RepoGraph(
  val id: String,
  val owner: String,
  val name: String,
  val description: String?
) {

  val releases = mutableListOf<ReleaseGraph>()

//  companion object {
//    fun from(owner: String, node: GithubQuery.Node) = RepoGraph(
//      id = node.id(),
//      owner = owner,
//      name = node.name(),
//      description = node.description()
//    )
//
//    fun from(owner: String, node: GithubQuery.Node3) = RepoGraph(
//      id = node.id(),
//      owner = owner,
//      name = node.name(),
//      description = node.description()
//    )
//
//    fun from(owner: String, node: ViewerRepositoriesAfterQuery.Node) = RepoGraph(
//      id = node.id(),
//      owner = owner,
//      name = node.name(),
//      description = node.description()
//    )
//
//    fun from(owner: String, node: OrganizationsAfterQuery.Node1) = RepoGraph(
//      id = node.id(),
//      owner = owner,
//      name = node.name(),
//      description = node.description()
//    )
//
//    fun from(owner: String, node: OrganizationRepositoriesAfterQuery.Node) = RepoGraph(
//      id = node.id(),
//      owner = owner,
//      name = node.name(),
//      description = node.description()
//    )
//  }
}