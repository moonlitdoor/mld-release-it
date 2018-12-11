package com.moonlitdoor.release.it.domain.api

import com.moonlitdoor.release.it.domain.query.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GithubApi {

  @POST("/graphql")
  fun queryViewer(@Body body: ViewerQuery, @Query("fun") q: Int = 1): Call<Result<Viewer.Data>>

  @POST("/graphql")
  fun queryViewerRepositories(@Body body: ViewerRepositoryQuery, @Query("fun") q: Int = 2): Call<Result<Repository.ViewerData>>

  @POST("/graphql")
  fun queryViewerOrganizations(@Body body: OrganizationQuery, @Query("fun") q: Int = 3): Call<Result<Organization.Data>>

  @POST("/graphql")
  fun queryOrganizationRepositories(@Body body: OrganizationRepositoryQuery, @Query("fun") q: Int = 4): Call<Result<Repository.OrganizationData>>

  @POST("/graphql")
  fun queryReleases(@Body body: ReleaseQuery, @Query("fun") q: Int = 5): Call<Result<Release.Data>>

}
