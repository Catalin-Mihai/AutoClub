package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catasoft.autoclub.ui.main.profile.CarFragment
import com.catasoft.autoclub.ui.main.profile.ProfileFragment
import timber.log.Timber

const val ARG_USER_UID = "uid"
const val TABS_NUMBER = 2

class HomePageCollectionAdapter(
    fragment: Fragment, private val displayedUserUid: String?)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = TABS_NUMBER

    override fun createFragment(position: Int): Fragment {
        Timber.e("position: %d", position)

        var fragment: Fragment? = null

        when(position){
            0 -> {
                fragment = ProfileFragment()
            }

            1 -> {
                fragment = CarFragment()
            }
        }

        // Return a NEW fragment instance in createFragment(int)

        fragment?.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putString(ARG_USER_UID, displayedUserUid)
        }
        return fragment!!
    }
}