package com.moonlitdoor.release.it.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference

fun <T : Any?> DocumentReference.liveData(field: String, t: Class<T>): LiveData<T?> = MutableLiveData<T>().also {
  this.addSnapshotListener { documentSnapshot, _ ->
    documentSnapshot?.let { doc ->
      if (doc.exists()) {
        it.value = doc.get(field, t)
      }
    }
  }
}


