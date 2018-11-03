package com.moonlitdoor.release.it.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.observe(owner: LifecycleOwner, block: (T) -> Unit) {
  this.observe(owner, Observer { block.invoke(it) })
}

fun <X, Y> LiveData<X>.map(block: (X) -> Y): LiveData<Y> {
  return Transformations.map(this, block)
}

fun <X> LiveData<X>.and(block: (X) -> Unit): LiveData<X> {
  return Transformations.map(this) {
    block.invoke(it)
    it
  }
}