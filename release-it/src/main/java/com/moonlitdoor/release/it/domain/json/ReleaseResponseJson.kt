package com.moonlitdoor.release.it.domain.json

class ReleaseResponseJson(
  val url: String,
  val html_url: String,
  val assets_url: String,
  val upload_url: String,
  val tarball_url: String,
  val zipball_url: String,
  val id: Long,
  val node_id: String,
  val tag_name: String,
  val target_commitish: String,
  val name: String,
  val body: String,
  val draft: Boolean,
  val prerelease: Boolean,
  val created_at: String, //"2013-02-27T19:35:32Z"
  val published_at: String, //: "2013-02-27T19:35:32Z"
  val author: AuthorJson,
  val assets: List<AssetJson>
)