package com.moonlitdoor.release.it.domain.query

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.moonlitdoor.release.it.ReleasesAfterQuery


class ReleasesAfterQueryProvider(private val client: ApolloClient) {

  fun get(owner: String, name: String, after: String): ApolloQueryCall<ReleasesAfterQuery.Data> = client.query(
    ReleasesAfterQuery.builder()
      .releases(QueryLimits.RELEASE_NODES)
      .owner(owner)
      .name(name)
      .after(after)
      .build()
  )
}