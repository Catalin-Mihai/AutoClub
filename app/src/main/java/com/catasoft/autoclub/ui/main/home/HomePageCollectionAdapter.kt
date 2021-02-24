package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catasoft.autoclub.ui.main.profile.ProfileFragment
import timber.log.Timber

const val ARG_OBJECT = "object"
const val TABS_NUMBER = 2

class HomePageCollectionAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = TABS_NUMBER

    override fun createFragment(position: Int): Fragment {
        Timber.e("position: %d", position)
        // Return a NEW fragment instance in createFragment(int)
        val fragment = ProfileFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}