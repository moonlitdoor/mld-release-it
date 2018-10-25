package com.moonlitdoor.release.it.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.navigation.NavigationView

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun bindItems(navigationView: NavigationView, items: List<String>?) {
  items?.forEach {
    navigationView.menu.add(it)
  }
}