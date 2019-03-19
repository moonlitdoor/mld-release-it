package com.moonlitdoor.release.it.domain.api

import com.moonlitdoor.release.it.domain.query.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GithubApi {

  @POST("/graphql")
  fun queryViewer(@Body body: ViewerQuery): Call<Result<ViewerData>>

  @POST("/graphql")
  fun queryOrganization(@Body body: OrganizationQuery): Call<Result<OrganizationData>>

  @POST("/graphql")
  fun queryViewerRepositories(@Body body: RepositoryViewerQuery): Call<Result<RepositoryViewerData>>

  @POST("/graphql")
  fun queryViewerOrganizations(@Body body: OrganizationQuery): Call<Result<OrganizationData>>

  @POST("/graphql")
  fun queryOrganizationRepositories(@Body body: RepositoryOrganizationQuery): Call<Result<RepositoryOrganizationData>>

  @POST("/graphql")
  fun queryReleases(@Body body: ReleaseQuery): Call<Result<ReleaseData>>

  @POST("/graphql")
  fun queryBranches(@Body body: BranchQuery): Call<Result<BranchData>>

}
