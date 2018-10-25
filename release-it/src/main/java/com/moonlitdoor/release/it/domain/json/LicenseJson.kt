package com.moonlitdoor.release.it.domain.json

//@JsonClass(generateAdapter = true)
class LicenseJson(
  val key: String,
  val name: String,
  val spdx_id: String,
  val url: String,
  val node_id: String
)