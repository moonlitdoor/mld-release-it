package com.moonlitdoor.release.it.domain.query

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.moonlitdoor.release.it.OrganizationRepositoriesAfterQuery


class OrganizationsRepositoriesAfterQueryProvider(private val client: ApolloClient) {

  fun get(login: String, after: String): ApolloQueryCall<OrganizationRepositoriesAfterQuery.Data> = client.query(
    OrganizationRepositoriesAfterQuery.builder()
      .releases(QueryLimits.RELEASE_NODES)
      .repositories(QueryLimits.REPOSITORY_NODES)
      .login(login)
      .after(after)
      .build()
  )
}