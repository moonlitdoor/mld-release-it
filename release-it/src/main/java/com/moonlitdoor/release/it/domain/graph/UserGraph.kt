package com.moonlitdoor.release.it.domain.graph

import org.threeten.bp.ZonedDateTime

class UserGraph(
  val id: String,
  val login: String,
  val name: String?,
  val email: String,
  // TODO Stop faking it
  val created: ZonedDateTime = ZonedDateTime.now(),
  val avatarUrl: String = "?"
) {
  companion object {
//    fun from(viewer: GithubQuery.Viewer) = UserGraph(
//      id = viewer.id(),
//      login = viewer.login(),
//      name = viewer.name(),
//      email = viewer.email()
//    )
  }
}