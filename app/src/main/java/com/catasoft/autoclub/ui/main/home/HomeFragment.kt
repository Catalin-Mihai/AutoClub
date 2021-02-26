package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentHomeBinding
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.ui.main.register.RegisterMyCarFragmentDirections
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(){

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pageCollectionAdapter: HomePageCollectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

//        binding.toolbar.setupWithNavController(findNavController())

//        binding.toolbarLayout.setupWithNavController(binding.toolbar, findNavController())
//        binding.toolbar.text

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e("HOME ESTE CREAT LOL?")

        val userUidToBeDisplayed = CurrentUser.getUid()

        pageCollectionAdapter = HomePageCollectionAdapter(this, userUidToBeDisplayed)
        binding.viewPager.adapter = pageCollectionAdapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = resources.getText(R.string.profile_tab1)
                1 -> tab.text = resources.getText(R.string.profile_tab2)
            }
        }.attach()

        binding.fabEdit.setOnClickListener{
            val navController = findNavController()
            val action = HomeFragmentDirections.actionHomeToProfileEditFragment()
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.home, false).build()
            navController.navigate(action,  navOptions)
        }

        Timber.e("AM AJUNS IN MY PROFILE!")
        viewModel.getProfileName()

    }
}

