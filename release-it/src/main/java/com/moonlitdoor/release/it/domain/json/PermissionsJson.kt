package com.moonlitdoor.release.it.domain.json

//@JsonClass(generateAdapter = true)
class PermissionsJson(
  val admin: Boolean,
  val push: Boolean,
  val pull: Boolean
)
