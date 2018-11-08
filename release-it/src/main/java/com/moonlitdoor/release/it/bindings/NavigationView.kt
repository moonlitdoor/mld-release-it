package com.moonlitdoor.release.it.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.navigation.NavigationView
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.domain.model.Repo

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun bindItems(navigationView: NavigationView, items: Collection<List<Repo>>?) {
  items?.forEach {
    if (it.isNotEmpty()) {
      navigationView.menu.addSubMenu(it[0].owner).also { subMenu ->
        it.forEachIndexed { index, repo -> subMenu.add(index, repo.id.toInt(), 0, repo.name).setIcon(R.drawable.ic_github).isCheckable = true }
      }
    }
  }
}