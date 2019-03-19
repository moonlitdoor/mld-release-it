package com.moonlitdoor.release.it.domain.query

class Branch(
    val id: String,
    val name: String,
    val target: Target
) {
  companion object {
    fun query(first: Int = QueryLimits.REF_NODES, owner: String, name: String, after: String? = null) =
        BranchQuery(
            """
              ${fragment()}

              query {
                repository(owner: "$owner", name: "$name") {
                  name
                  ${Nodes.field("refs", first, "Branch", after, "refs/heads/")}
                }
              }
            """.trimIndent()
        )

    fun fragment() =
        """
          fragment Branch on Ref {
            id
            name
            ${Target.field()}
          }
        """.trimIndent()
  }
}