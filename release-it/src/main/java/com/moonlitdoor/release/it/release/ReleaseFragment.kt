package com.moonlitdoor.release.it.release

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moonlitdoor.release.it.databinding.FragmentReleaseBinding
import org.koin.android.ext.android.inject

class ReleaseFragment : Fragment() {

  private val viewModel: ReleaseViewModel by inject()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
    FragmentReleaseBinding.inflate(inflater, container, false).also {
      it.viewModel = viewModel
      it.setLifecycleOwner(this)
      it.navigationView.setNavigationItemSelectedListener { item ->
        true
      }
//      it.recyclerView.adapter = Adapter(inflater)
    }.root

//  private class Adapter(private val layoutInflater: LayoutInflater) : ListAdapter<Repo, RepoViewHolder>(object : DiffUtil.ItemCallback<Repo>() {
//    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem.id == newItem.id
//    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean = oldItem == newItem
//  }) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepoViewHolder(ListItemRepoBinding.inflate(layoutInflater, parent, false))
//    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) = holder.bind(getItem(position)).ignore()
//  }
//
//  private class RepoViewHolder(private val binding: ListItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
//    fun bind(repo: Repo) = binding.also {
//      it.repo = repo
//    }
//  }

}