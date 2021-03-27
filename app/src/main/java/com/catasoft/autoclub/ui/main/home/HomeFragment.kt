package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentHomeBinding
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.ui.main.profile.ProfileFragment
import com.catasoft.autoclub.util.getNavigationResultLiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val PROFILE_TAB: Int = 0
const val CARS_TAB: Int = 1

@AndroidEntryPoint
class HomeFragment : BaseFragment(){

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var pageCollectionAdapter: HomePageCollectionAdapter

    override fun onResume() {
        super.onResume()
        Timber.e("HOME RESUMED!")
    }

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

        Timber.e("SE CREAZA HOME")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Timber.e("HOME ESTE CREAT LOL?")

        val userUidToBeDisplayed = CurrentUser.getUid()

        pageCollectionAdapter = HomePageCollectionAdapter(this, userUidToBeDisplayed)
        binding.viewPager.adapter = pageCollectionAdapter

        //Change the fab icon based on the context
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    PROFILE_TAB -> binding.fab.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_mode_edit_24px) })
                    CARS_TAB -> binding.fab.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_add_24px) })
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                PROFILE_TAB -> tab.text = resources.getText(R.string.profile_tab1)
                CARS_TAB -> tab.text = resources.getText(R.string.profile_tab2)
            }
        }.attach()


        binding.fab.setOnClickListener{
            when(binding.viewPager.currentItem) {
                PROFILE_TAB -> {
                    val navController = findNavController()
                    val action = HomeFragmentDirections.actionHomeToProfileEditFragment()
                    val navOptions = NavOptions.Builder().setPopUpTo(R.id.home, false).build()
                    navController.navigate(action,  navOptions)
                }
                CARS_TAB -> {
                    //Start the new car addition fragment
                    val navController = findNavController()
                    val action = HomeFragmentDirections.actionHomeToAddCarActivity()
                    navController.navigate(action)
                }
            }
        }

        //When a user finish the car addition fragment
        val result = this.getNavigationResultLiveData<String>("test")
        //Do not convert to lambda. It will not create a new instance
        result?.observe(viewLifecycleOwner, object: Observer<String>{
            override fun onChanged(t: String?) {
                Timber.e("VALUE RECEIVED: $t")
            }
        })

        Timber.e("AM AJUNS IN MY PROFILE!")
        viewModel.getAvatarDownloadUri()

        viewModel.avatarUri.observe(viewLifecycleOwner, {
            //Load User photo
            Picasso.get().load(it).into(binding.ivPhoto)
        })
    }
}

