package com.moonlitdoor.release.it.domain.repository

import com.moonlitdoor.release.it.domain.dao.AuthDao

class AuthRepository(private val authDao: AuthDao) {

  fun setAuthToken(authToken: String?) = authDao.setAuthToken(authToken)

  val authToken = authDao.authToken

}