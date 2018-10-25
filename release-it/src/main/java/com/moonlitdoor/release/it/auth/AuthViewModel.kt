package com.moonlitdoor.release.it.auth

import androidx.lifecycle.ViewModel
import com.moonlitdoor.release.it.domain.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
  fun setAuthToken(authToken: String?) = authRepository.setAuthToken(authToken)
}