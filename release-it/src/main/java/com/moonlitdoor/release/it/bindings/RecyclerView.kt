package com.moonlitdoor.release.it.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
@BindingAdapter("items")
fun <T, VH : RecyclerView.ViewHolder> bindItems(recyclerView: RecyclerView, items: List<T>?) {
  items ?: return
  (recyclerView.adapter as? ListAdapter<T, VH>)?.submitList(items) ?: error("Must use a 'androidx.recyclerview.widget.ListAdapter' for app:items")
}