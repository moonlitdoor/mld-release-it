package com.moonlitdoor.release.it.domain.query

class Release(
    val id: String,
    val name: String?,
    val description: String?,
    val isDraft: Boolean,
    val tag: Tag?
) {
  companion object {
    fun query(first: Int = QueryLimits.RELEASE_NODES, owner: String, name: String, after: String? = null) =
        ReleaseQuery(
            """
        ${fragment()}

        query {
          repository(owner: "$owner", name: "$name") {
            name
            ${Nodes.field("releases", first, "Release", after)}
          }
        }
      """.trimIndent()
    )

    fun fragment() = """
      fragment Release on Release {
        id
        name
        description
        isDraft
        ${Tag.query()}
      }
    """.trimIndent()
  }


}