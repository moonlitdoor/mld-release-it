package com.moonlitdoor.release.it.component

import androidx.lifecycle.ViewModel
import com.moonlitdoor.release.it.extension.ignore
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.Job
import kotlin.coroutines.experimental.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

  private val job = Job()

  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO + job

  override fun onCleared() = job.cancel().ignore()

}