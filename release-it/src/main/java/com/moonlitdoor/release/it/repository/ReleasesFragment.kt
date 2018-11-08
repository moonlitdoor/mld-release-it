package com.moonlitdoor.release.it.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.databinding.FragmentReleasesBinding
import com.moonlitdoor.release.it.databinding.ListItemReleaseBinding
import com.moonlitdoor.release.it.domain.model.Release
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ReleasesFragment : RegisterAdapter.TitledFragment() {

  private val viewModel by sharedViewModel<RepositoryViewModel>()
  private val adapter by lazy { Adapter(LayoutInflater.from(activity)) }

  override fun getTitleResource() = R.string.releases

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
    DataBindingUtil.inflate<FragmentReleasesBinding>(inflater, R.layout.fragment_releases, container, false).also {
      it.viewModel = viewModel
      it.setLifecycleOwner(this)
      it.recyclerView.adapter = adapter
    }.root

  private class Adapter(private val layoutInflater: LayoutInflater) : ListAdapter<Release, EntryViewHolder>(object : DiffUtil.ItemCallback<Release>() {
    override fun areItemsTheSame(oldItem: Release, newItem: Release): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Release, newItem: Release): Boolean = oldItem == newItem
  }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EntryViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.list_item_release, parent, false))
    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) = holder.bind(getItem(position)).ignore()
  }

  private class EntryViewHolder(private val binding: ListItemReleaseBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(release: Release) = binding.also {
      it.release = release
    }
  }

}