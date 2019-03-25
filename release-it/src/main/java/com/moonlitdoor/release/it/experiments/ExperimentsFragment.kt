package com.moonlitdoor.release.it.experiments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.AdapterViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.databinding.FragmentExperimentsBinding
import com.moonlitdoor.release.it.databinding.ListItemExperimentBinding
import com.moonlitdoor.release.it.extension.ignore
import org.koin.android.viewmodel.ext.android.viewModel

class ExperimentsFragment : Fragment() {

  private val viewModel by viewModel<ExperimentsViewModel>()
  private val adapter by lazy { Adapter(LayoutInflater.from(activity), this) }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      DataBindingUtil.inflate<FragmentExperimentsBinding>(inflater, R.layout.fragment_experiments, container, false).also {
        it.toolbar.setupWithNavController(findNavController(), AppBarConfiguration(findNavController().graph))
        it.viewModel = viewModel
        it.lifecycleOwner = this
        it.recyclerView.adapter = adapter
      }.root

  private class Adapter(private val layoutInflater: LayoutInflater,
                        private val lifecycleOwner: LifecycleOwner) : ListAdapter<Experiment<*>, ExperimentViewHolder>(object : DiffUtil.ItemCallback<Experiment<*>>() {
    override fun areItemsTheSame(oldItem: Experiment<*>, newItem: Experiment<*>): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Experiment<*>, newItem: Experiment<*>): Boolean = oldItem == newItem
  }) {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ExperimentViewHolder = ExperimentViewHolder(
        DataBindingUtil.inflate(layoutInflater, R.layout.list_item_experiment, parent, false), lifecycleOwner)

    override fun onBindViewHolder(holder: ExperimentViewHolder, position: Int) = holder.bind(getItem(position)).ignore()
  }


  private class ExperimentViewHolder(
      private val binding: ListItemExperimentBinding, private val lifecycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(
      binding.root), AdapterViewBindingAdapter.OnItemSelected {

    fun bind(experiment: Experiment<*>) = binding.also {
      it.experiment = experiment
      it.handler = this
      it.lifecycleOwner = lifecycleOwner
    }.ignore()

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
        binding.experiment?.let {
          it.local = it.options[position] as String
        }.ignore()


  }


}