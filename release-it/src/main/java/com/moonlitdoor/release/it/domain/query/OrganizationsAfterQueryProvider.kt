package com.moonlitdoor.release.it.domain.query

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.moonlitdoor.release.it.OrganizationsAfterQuery


class OrganizationsAfterQueryProvider(private val client: ApolloClient) {

  fun get(after: String): ApolloQueryCall<OrganizationsAfterQuery.Data> = client.query(
    OrganizationsAfterQuery.builder()
      .releases(QueryLimits.RELEASE_NODES)
      .repositories(QueryLimits.REPOSITORY_NODES)
      .organizations(QueryLimits.ORGANIZATION_NODES)
      .after(after)
      .build()
  )
}