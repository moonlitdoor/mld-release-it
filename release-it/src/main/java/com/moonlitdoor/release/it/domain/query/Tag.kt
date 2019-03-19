package com.moonlitdoor.release.it.domain.query

class Tag(
    val id: String,
    val name: String,
    val target: Target
) {
  companion object {
    fun query() =
        """
          tag {
            id
            name
            ${Target.field()}
          }
        """.trimIndent()
  }
}