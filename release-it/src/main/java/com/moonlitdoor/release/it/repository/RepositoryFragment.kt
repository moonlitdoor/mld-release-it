package com.moonlitdoor.release.it.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.databinding.FragmentRepositoryBinding
import com.moonlitdoor.release.it.extension.observe
import org.koin.android.viewmodel.ext.android.sharedViewModel

class RepositoryFragment : Fragment() {

  private val viewModel by sharedViewModel<RepositoryViewModel>()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      FragmentRepositoryBinding.inflate(inflater, container, false).also { binding ->
        viewModel.authToken.observe(this) { token ->
          token?.let {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            binding.toolbar.inflateMenu(R.menu.repository)
            binding.toolbar.setOnMenuItemClickListener {
              when (it.itemId) {
                R.id.settings -> findNavController().navigate(R.id.fragment_settings)
              }
              true
            }
            binding.tabLayout.setupWithViewPager(binding.viewPager)
            binding.drawerLayout.addDrawerListener(
                ActionBarDrawerToggle(activity, binding.drawerLayout, binding.toolbar, R.string.drawer_open, R.string.drawer_close).apply {
                  syncState()
                })
            binding.navigationView.setNavigationItemSelectedListener { menuItem ->
              viewModel.id = menuItem.itemId.toLong()
              binding.tabLayout.visibility = View.VISIBLE
              binding.drawerLayout.closeDrawers()
              activity?.let { act ->
                binding.viewPager.adapter = RegisterAdapter(act)
              }
              true
            }
          } ?: findNavController().navigate(R.id.fragment_auth)
        }
      }.root
}