package com.moonlitdoor.release.it.domain.query

class BranchData(
    val repository: Repository
) {
  class Repository(
      val name: String,
      val refs: Nodes<Branch>
  )
}
