package com.moonlitdoor.release.it.domain.query

class Target(
    val id: String,
    val oid: String
) {
  companion object {
    fun field() =
        """
          target {
            id
            oid
          }
        """.trimIndent()
  }
}