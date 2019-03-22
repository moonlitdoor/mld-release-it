package com.moonlitdoor.release.it.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.*
import com.moonlitdoor.release.it.BuildConfig
import com.moonlitdoor.release.it.R
import com.moonlitdoor.release.it.databinding.FragmentSettingsBinding
import com.moonlitdoor.release.it.util.Analytics

sealed class SettingsFragment(@StringRes private val title: Int,
                              private val hierarchy: (context: Context, fragment: Fragment, screen: PreferenceScreen) -> Unit) : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      FragmentSettingsBinding.inflate(inflater, container, false).also {
        it.toolbar.setTitle(title)
        it.toolbar.setupWithNavController(findNavController(), AppBarConfiguration(findNavController().graph))
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.settings_fragment_container, SettingsFragment.Pref(hierarchy))?.commit()
      }.root

  class Pref(
      private val hierarchy: (context: Context, fragment: Fragment, screen: PreferenceScreen) -> Unit) : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
      preferenceManager.context.also { context ->
        preferenceManager.createPreferenceScreen(context).also {
          preferenceScreen = it
          hierarchy.invoke(context, this, it)
        }
      }
    }
  }

  class Root : SettingsFragment(R.string.settings, { context, fragment, screen ->

    var experimentClicks = 0
    var toast: Toast? = null

    PreferenceCategory(context).also { category ->
      screen.addPreference(category)
      category.title = context.getString(R.string.info)
      category.addPreference(Preference(context).also {
        it.key = SETTING_VERSION
        it.title = context.getString(R.string.version)
        it.summary = BuildConfig.VERSION_NAME
        it.setOnPreferenceClickListener { pref ->
          toast?.cancel()
          toast = when (experimentClicks) {
            4 -> Toast.makeText(context, "Five more clicks to enable Experiments.", Toast.LENGTH_LONG)
            5 -> Toast.makeText(context, "4...", Toast.LENGTH_LONG)
            6 -> Toast.makeText(context, "3...", Toast.LENGTH_LONG)
            7 -> Toast.makeText(context, "2...", Toast.LENGTH_LONG)
            8 -> Toast.makeText(context, "1...", Toast.LENGTH_LONG)
            9 -> {
              pref.sharedPreferences.edit().putBoolean(EXPERIMENTS_ENABLED, true).apply()
              pref.preferenceManager.findPreference<PreferenceCategory>(EXPERIMENTS_KEY)?.isVisible = true
              Analytics.experimentsEnabledFromSettings()
              Toast.makeText(context, "Experiments Enabled.", Toast.LENGTH_LONG)
            }
            else -> null
          }
          toast?.show()
          experimentClicks++
          true
        }
      })
      category.addPreference(Preference(context).also {
        it.key = SETTING_BUILD_TYPE
        it.title = "Build Type"
        it.summary = BuildConfig.BUILD_TYPE
        it.isVisible = BuildConfig.DEBUG
      })
      category.addPreference(Preference(context).also {
        it.key = SETTING_FLAVOR
        it.title = "Flavor"
        it.summary = BuildConfig.FLAVOR
        it.isVisible = BuildConfig.DEBUG
      })
    }
    PreferenceCategory(context).also { category ->
      screen.addPreference(category)
      category.title = context.getString(R.string.other)
      category.addPreference(Preference(context).also {
        it.title = context.getString(R.string.sync)
        it.setOnPreferenceClickListener {
          fragment.findNavController().navigate(R.id.fragment_settings_sync)
          true
        }
      })
    }

    PreferenceCategory(context).also { category ->
      screen.addPreference(category)
      category.key = EXPERIMENTS_KEY
      category.isVisible = category.sharedPreferences.getBoolean(EXPERIMENTS_ENABLED, false)
      category.title = context.getString(R.string.experiments)
      category.addPreference(Preference(context).also {
        it.title = context.getString(R.string.experiments)
        it.setOnPreferenceClickListener {
          fragment.findNavController().navigate(R.id.fragment_experiments)
          true
        }
      })
    }
  })

  class Sync : SettingsFragment(R.string.sync, { context, _, screen ->
    PreferenceCategory(context).also { category ->
      screen.addPreference(category)
      category.title = context.getString(R.string.sync)
      category.addPreference(SwitchPreferenceCompat(context).also {
        it.key = SETTING_ENABLE_SYNC
        it.title = context.getString(R.string.periodic_syncing)
        it.summary = BuildConfig.VERSION_NAME
        it.summaryProvider = Preference.SummaryProvider<SwitchPreferenceCompat> { pref ->
          when (pref.isChecked) {
            true -> context.getString(R.string.last_synced)
            false -> context.getString(R.string.sync_disabled)
          }
        }
      })
      EditTextPreference(context).also {
        category.addPreference(it)
        it.key = SETTING_SYNC_CODE
        it.title = context.getString(R.string.sync_code)
        it.dependency = SETTING_ENABLE_SYNC
        it.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
      }
    }
  })

  companion object {
    const val SETTING_VERSION = "com.moonlitdoor.release.it.settings.VERSION"
    const val SETTING_BUILD_TYPE = "com.moonlitdoor.release.it.settings.BUILD_TYPE"
    const val SETTING_FLAVOR = "com.moonlitdoor.release.it.settings.FLAVOR"
    const val SETTING_ENABLE_SYNC = "com.moonlitdoor.release.it.settings.ENABLE_SYNC"
    const val SETTING_SYNC_CODE = "com.moonlitdoor.release.it.settings.SYNC_CODE"
    const val EXPERIMENTS_ENABLED = "sdfasdjkdfnaksgbfkadsfngwrjkfbrqkdjnfskdnbfaljsndqonfou2bn3rtiogwhsvzzndfoqn"
    const val EXPERIMENTS_KEY = "sdfasdjawet24gaiefn1389rhgeiuksfasfdn0q823ru023foslefignworhgwkdfnaksgbfkads"

  }

}