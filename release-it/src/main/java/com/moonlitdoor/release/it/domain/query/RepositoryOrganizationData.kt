package com.moonlitdoor.release.it.domain.query

class RepositoryOrganizationData(
    val organization: Organization
) {
  class Organization(
      val login: String,
      val repositories: Nodes<Repository>
  )
}