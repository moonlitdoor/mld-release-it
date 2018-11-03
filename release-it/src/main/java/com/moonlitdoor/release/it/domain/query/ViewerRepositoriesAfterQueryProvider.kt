package com.moonlitdoor.release.it.domain.query

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.moonlitdoor.release.it.ViewerRepositoriesAfterQuery


class ViewerRepositoriesAfterQueryProvider(private val client: ApolloClient) {

  fun get(after: String): ApolloQueryCall<ViewerRepositoriesAfterQuery.Data> = client.query(
    ViewerRepositoriesAfterQuery.builder()
      .releases(QueryLimits.RELEASE_NODES)
      .repositories(QueryLimits.REPOSITORY_NODES)
      .after(after)
      .build()
  )
}