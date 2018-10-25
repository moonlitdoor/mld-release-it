package com.moonlitdoor.release.it.splash

import androidx.lifecycle.ViewModel
import com.moonlitdoor.release.it.domain.repository.AuthRepository

class SplashViewModel(authRepository: AuthRepository) : ViewModel() {

  val authToken = authRepository.authToken

}