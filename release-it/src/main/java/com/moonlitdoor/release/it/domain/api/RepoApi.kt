package com.moonlitdoor.release.it.domain.api

import com.moonlitdoor.release.it.domain.json.RepoJson
import retrofit2.Call
import retrofit2.http.GET

interface RepoApi {

  @GET("/user/repos")
  fun getRepos(): Call<List<RepoJson>>

}