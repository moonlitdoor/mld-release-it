package com.moonlitdoor.release.it.component

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moonlitdoor.release.it.extension.ignore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + job

  override fun onCleared() = job.cancel().ignore()

}