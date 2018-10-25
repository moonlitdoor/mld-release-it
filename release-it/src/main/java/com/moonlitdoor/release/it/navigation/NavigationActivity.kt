package com.moonlitdoor.release.it.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.moonlitdoor.release.it.R

class NavigationActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

}
