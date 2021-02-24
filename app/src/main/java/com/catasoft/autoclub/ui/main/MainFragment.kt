package com.catasoft.autoclub.ui.main

import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentMainBinding
import com.catasoft.autoclub.ui.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        Timber.e("Main Fragment is inflating...")
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // These lines should have been in the onViewCreated event
        // But that's a bug with the androidX fragmentContainerView
        // not finishing transactions, so navHost can't be retrieved
        // onResume is surely called after the transactions
        // being the last event in the lifecycle of a fragment

        val navController = binding.navFragment.findNavController()
        val bottomNav = binding.bottomNavigation

        bottomNav.setupWithNavController(navController)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e("Main Fragment has been inflated!")

        //Todo: Add animations for fragment transitions.
        // Make actions (with destination animation) in the nav .xml and manually set the destinations
        // based on the clicked menu item

    }
}
