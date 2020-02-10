package com.moonlitdoor.release.it.component

import android.content.Context
import android.view.View
import androidx.annotation.IdRes

class Tooltip {


  class Builder(private val context: Context, @IdRes id: Int) {


    companion object {
      fun invoke(target: View) = Builder(target.context, target.id)
    }

  }

  companion object {
    fun defaults() {}
  }

}