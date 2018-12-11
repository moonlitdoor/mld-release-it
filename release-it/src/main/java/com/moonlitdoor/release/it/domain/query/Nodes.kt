package com.moonlitdoor.release.it.domain.query

class Nodes<T>(
  val totalCount: Int,
  val pageInfo: PageInfo,
  val nodes: List<T>
) {
  companion object {
    fun query(field: String, first: Int, fragment: String, after: String? = null) = """
      $field(first: $first, after: ${after?.let { """ "$it" """ }}) {
        totalCount
        pageInfo {
          endCursor
        }
        nodes {
          ...$fragment
        }
      }
    """.trimIndent()
  }

  class PageInfo(
    val endCursor: String?
  )
}