package com.moonlitdoor.release.it.domain.json

class AssetJson(
  val url: String,
  val browser_download_url: String,
  val id: Long,
  val node_id: String,
  val name: String,
  val label: String,
  val state: String,
  val content_type: String,
  val size: Long,
  val download_count: Long,
  val created_at: String,
  val updated_at: String,
  val uploader: AuthorJson
)