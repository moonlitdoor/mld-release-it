package com.moonlitdoor.release.it.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.navigation.NavigationView
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.domain.model.Repository

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun NavigationView.bindItems(items: Collection<List<Repository>>?) {
  this.menu.clear()
  items?.forEach {
    if (it.isNotEmpty()) {
      this.menu.addSubMenu(it[0].owner).also { subMenu ->
        it.forEachIndexed { index, repository ->
          subMenu.add(index, repository.id.toInt(), 0, repository.name).setIcon(R.drawable.ic_github).isCheckable = true
        }
      }
    }
  }
}