package com.moonlitdoor.release.it.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.moonlitdoor.release.it.R

class NavigationActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_navigation)
  }

  override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

  companion object {
    fun start(context: Context) = context.startActivity(Intent(context, NavigationActivity::class.java))
  }

}
