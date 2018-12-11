package com.moonlitdoor.release.it.domain.query

class Release(
  val id: String,
  val name: String?,
  val description: String?,
  val isDraft: Boolean,
  val tag: Tag?
) {
  companion object {
    fun query(first: Int = QueryLimits.RELEASE_NODES, owner: String, name: String, after: String? = null) = ReleaseQuery(
      """
        ${fragment()}

        query {
          repository(owner: "$owner", name: "$name") {
            name
            ${Nodes.query("releases", first, "Release", after)}
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
        tag {
          prefix
          id
          name
          target {
            id
            abbreviatedOid
            oid
          }
        }
      }
    """.trimIndent()
  }

  class Data(
    val repository: Repository
  ) {
    class Repository(
      val name: String,
      val releases: Nodes<Release>
    )
  }


  class Tag(
    val prefix: String,
    val id: String,
    val name: String,
    val target: Target
  ) {
    class Target(
      val id: String,
      val abbreviatedOid: String,
      val oid: String
    )
  }
}