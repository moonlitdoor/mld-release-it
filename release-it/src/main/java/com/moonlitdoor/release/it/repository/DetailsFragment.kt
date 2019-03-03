package com.moonlitdoor.release.it.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.databinding.FragmentDetailsBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DetailsFragment : RegisterAdapter.TitledFragment() {

  private val viewModel by sharedViewModel<RepositoryViewModel>()

  override fun getTitleResource() = R.string.details

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
    DataBindingUtil.inflate<FragmentDetailsBinding>(inflater, R.layout.fragment_details, container, false).also {
      it.viewModel = viewModel
      it.lifecycleOwner = this
    }.root
}