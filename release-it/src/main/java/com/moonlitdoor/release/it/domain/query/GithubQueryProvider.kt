package com.moonlitdoor.release.it.domain.query

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.moonlitdoor.release.it.GithubQuery


class GithubQueryProvider(private val client: ApolloClient) {

  fun get(): ApolloQueryCall<GithubQuery.Data> = client.query(
    GithubQuery.builder()
      .releases(QueryLimits.RELEASE_NODES)
      .repositories(QueryLimits.REPOSITORY_NODES)
      .organizations(QueryLimits.ORGANIZATION_NODES)
      .build()
  )
}