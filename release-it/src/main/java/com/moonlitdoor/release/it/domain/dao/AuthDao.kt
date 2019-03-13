package com.moonlitdoor.release.it.domain.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class AuthDao(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

  fun setAuthToken(authToken: String?) = authToken?.let { token ->
    auth.signInWithCredential(GithubAuthProvider.getCredential(token)).addOnCompleteListener { task ->
      task.result?.let { result ->
        result.user?.let { user ->
          firestore.collection(COLLECTION_USERS).document(user.uid).set(mapOf(KEY_ACCESS_TOKEN to token))
              .addOnSuccessListener {
                this.user.value = user
              }
        }
      }
    }
  }

  val user = MutableLiveData<FirebaseUser?>().also {
    it.value = auth.currentUser
  }

  fun getAuthToken(): String? = authToken.value

  fun clearAuthToken() = user.value?.let {
    firestore.collection(COLLECTION_USERS).document(it.uid).delete()
  }

  val authToken: LiveData<String?> = MutableLiveData<String?>().also {
    auth.currentUser?.let { user ->
      firestore.collection(COLLECTION_USERS).document(user.uid).addSnapshotListener { documentSnapshot, _ ->
        it.value = documentSnapshot?.get(KEY_ACCESS_TOKEN, String::class.java)
      }
    }
  }

  companion object {
    private const val COLLECTION_USERS = "users"
    private const val KEY_ACCESS_TOKEN = "accessToken"
  }

}