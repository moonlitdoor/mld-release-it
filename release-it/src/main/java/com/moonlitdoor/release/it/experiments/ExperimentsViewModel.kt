package com.moonlitdoor.release.it.experiments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moonlitdoor.release.it.component.BaseViewModel

class ExperimentsViewModel : BaseViewModel() {

  val experiments: LiveData<List<Experiment<*>>> = MutableLiveData<List<Experiment<*>>>().also { it.value = Experiments.experiments }

}