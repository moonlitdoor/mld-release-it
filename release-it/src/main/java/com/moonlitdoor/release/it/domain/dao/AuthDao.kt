package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.moonlitdoor.release.it.extension.liveData
import com.moonlitdoor.release.it.extension.switchMap

class AuthDao(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

  fun setAuthToken(authToken: String?) = authToken?.let { token ->
    auth.signInWithCredential(GithubAuthProvider.getCredential(token)).addOnCompleteListener { task ->
      task.result?.user?.let {
        firestore.collection(COLLECTION_USERS).document(it.uid).set(mapOf(KEY_ACCESS_TOKEN_GITHUB to token))
            .addOnSuccessListener { _ ->
              user.value = it
            }
      }
    }
  }

  private val user = MutableLiveData<FirebaseUser?>().also {
    it.value = auth.currentUser
  }

  fun getAuthToken(): String? = authToken.value

  fun clearAuthToken() = user.value?.let {
    firestore.collection(COLLECTION_USERS).document(it.uid).update(KEY_ACCESS_TOKEN_GITHUB, null)
  }

  val authToken: LiveData<String?> = MutableLiveData<String?>().also {
    it.value = "c721796d39fe7626deaa8e1433b9027e3a4d21d0"
  }

  val authToken2: LiveData<String?> = user.switchMap { user ->
    user?.let {
      firestore.collection(COLLECTION_USERS).document(user.uid).liveData(KEY_ACCESS_TOKEN_GITHUB, String::class.java)
    } ?: MutableLiveData<String?>().also { it.value = null }
  }

  companion object {
    private const val COLLECTION_USERS = "users"
    private const val KEY_ACCESS_TOKEN_GITHUB = "accessTokenGithub"
  }

}