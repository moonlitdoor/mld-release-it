package com.moonlitdoor.release.it.domain.api

import com.moonlitdoor.release.it.domain.json.ReleaseJson
import com.moonlitdoor.release.it.domain.json.ReleaseResponseJson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReleaseApi {

  @POST("/repos/{owner}/{repository}/releases")
  fun create(@Path("owner") owner: String, @Path("repository") repository: String, @Body release: ReleaseJson): Call<ReleaseResponseJson>

}