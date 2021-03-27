package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.catasoft.autoclub.databinding.CarProfileCardBinding
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.ui.main.profilecars.CarsFragment
import com.catasoft.autoclub.ui.main.profile.ProfileFragment
import timber.log.Timber

const val ARG_USER_UID = "uid"
const val ARG_USER_CARS = "cars"
const val TABS_NUMBER = 2

class HomePageCollectionAdapter(
    fragment: Fragment, private val displayedUserUid: String?)
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = TABS_NUMBER

    override fun createFragment(position: Int): Fragment {
        Timber.e("position: %d", position)

        var fragment: Fragment? = null

        when(position){
            PROFILE_TAB -> {
                fragment = ProfileFragment()
                fragment.arguments = Bundle().apply {
                    putString(ARG_USER_UID, displayedUserUid)
                }
            }

            CARS_TAB -> {
                fragment = CarsFragment()

                fragment.arguments = Bundle().apply {
                    putString(ARG_USER_UID, displayedUserUid)
                }
            }
        }

        // Return a NEW fragment instance in createFragment(int)


        return fragment!!
    }
}