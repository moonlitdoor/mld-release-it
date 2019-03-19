package com.moonlitdoor.release.it.domain.query

class PageInfo(
    val endCursor: String?,
    val hasNextPage: Boolean
) {
  companion object {
    fun query() = """
      pageInfo {
        endCursor
        hasNextPage
      }
    """.trimIndent()
  }
}