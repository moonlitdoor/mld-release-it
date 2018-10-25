package com.moonlitdoor.release.it.domain.api

import com.moonlitdoor.release.it.domain.json.UserJson
import retrofit2.Call
import retrofit2.http.GET

interface UserApi {

  @GET("/user")
  fun getUser(): Call<UserJson>

}