package com.moonlitdoor.release.it.domain.query

class ReleaseData(
    val repository: Repository
) {
  class Repository(
      val name: String,
      val releases: Nodes<Release>
  )
}