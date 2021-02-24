package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentHomeBinding
import com.catasoft.autoclub.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment: BaseFragment(){

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

        pageCollectionAdapter = HomePageCollectionAdapter(this)
        binding.viewPager.adapter = pageCollectionAdapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0 -> tab.text = resources.getText(R.string.profile_tab1)
                1 -> tab.text = resources.getText(R.string.profile_tab2)
            }
        }.attach()


        Timber.e("AM AJUNS IN MY PROFILE!")
        viewModel.getProfileName()

//        viewModel.profileName.observe(viewLifecycleOwner, {
//            binding.textView.text = it
//        })
    }
}

