package com.moonlitdoor.release.it.domain.json

class ReleaseJson(
  val tag_name: String,
  val target_commitish: String,
  val name: String,
  val body: String,
  val draft: Boolean = false,
  val prerelease: Boolean = false
)