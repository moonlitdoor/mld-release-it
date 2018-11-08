package com.moonlitdoor.release.it.repository

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter

class RegisterAdapter(private val fragmentActivity: FragmentActivity) : FragmentPagerAdapter(fragmentActivity.supportFragmentManager) {

  private val fragments = listOf(DetailsFragment(), ReleasesFragment())

  override fun getItem(position: Int): Fragment = fragments[position]

  override fun getCount(): Int = fragments.size

  override fun getPageTitle(position: Int): CharSequence? = fragmentActivity.getString(fragments[position].getTitleResource())

  abstract class TitledFragment : Fragment() {
    abstract fun getTitleResource(): Int
  }

}