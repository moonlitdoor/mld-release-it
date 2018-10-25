package com.moonlitdoor.release.it.component

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.moonlitdoor.release.it.extension.ignore
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.Job
import kotlin.coroutines.experimental.CoroutineContext

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + job

  override fun onCleared() = job.cancel().ignore()

}