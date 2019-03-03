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
    FragmentRepositoryBinding.inflate(inflater, container, false).also {
      viewModel.authToken.observe(this) { token ->
        token ?: findNavController().navigate(R.id.fragment_settings)
//        token ?: findNavController().navigate(R.id.fragment_auth)
      }
      it.viewModel = viewModel
      it.lifecycleOwner = this
      it.tabLayout.setupWithViewPager(it.viewPager)
      it.drawerLayout.addDrawerListener(ActionBarDrawerToggle(activity, it.drawerLayout, it.toolbar, R.string.drawer_open, R.string.drawer_close).apply {
        syncState()
      })
      it.navigationView.setNavigationItemSelectedListener { menuItem ->
        viewModel.id = menuItem.itemId.toLong()
        it.tabLayout.visibility = View.VISIBLE
        it.drawerLayout.closeDrawers()
        activity?.let { act ->
          it.viewPager.adapter = RegisterAdapter(act)
        }
        true
      }
    }.root
}