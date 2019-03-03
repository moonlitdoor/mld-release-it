package com.moonlitdoor.release.it.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      FragmentSettingsBinding.inflate(inflater, container, false).also {
        findNavController().navigate(R.id.fragment_experiments)
      }.root

}