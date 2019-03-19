package com.moonlitdoor.release.it.domain.query

class Nodes<T>(
    val totalCount: Int,
    val pageInfo: PageInfo,
    val nodes: List<T>
) {
  companion object {
    fun field(field: String, first: Int, fragment: String, after: String? = null, refPrefix: String? = null) =
        """
          $field(first: $first ${after?.let { """, after: "$it" """ } ?: ""} ${refPrefix?.let { """ , refPrefix:"$it" """ } ?: ""}) {
            totalCount
            ${PageInfo.query()}
            nodes {
              ...$fragment
            }
          }
        """.trimIndent()
  }

}