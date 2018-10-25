package com.moonlitdoor.release.it.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.extension.observe
import org.koin.android.ext.android.inject

class SplashFragment : Fragment() {

  private val viewModel: SplashViewModel by inject()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
    inflater.inflate(R.layout.fragment_splash, container, false).also { _ ->
      viewModel.authToken.observe(this) {
        it?.let { _ ->
          NavHostFragment.findNavController(this).navigate(R.id.fragment_release)
        } ?: kotlin.run {
          Handler(Looper.getMainLooper()).postDelayed({
            NavHostFragment.findNavController(this).navigate(R.id.fragment_auth)
          }, 500)
        }
      }
    }

}