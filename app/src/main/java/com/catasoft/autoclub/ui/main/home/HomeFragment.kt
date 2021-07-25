package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentHomeBinding
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.BaseFragment
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
    private var userUid: String? = null


    override fun onResume() {
        super.onResume()
        Timber.e("HOME RESUMED!")
        //Refresh the tabs
        binding.viewPager.adapter = pageCollectionAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        userUid = arguments?.takeIf { it.containsKey(ARG_USER_UID) }?.getString(ARG_USER_UID) ?: CurrentUser.getUid()

        binding = FragmentHomeBinding.inflate(inflater)
        binding.userUid = userUid
        binding.lifecycleOwner = this



        Timber.e("SE CREAZA HOME")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAvatarDownloadUri(userUid!!)

        pageCollectionAdapter = HomePageCollectionAdapter(this, userUid)
        binding.viewPager.adapter = pageCollectionAdapter

        //Change the fab icon based on the context
        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                pageCollectionAdapter.notifyDataSetChanged()
//                pageCollectionAdapter.notifyItemRemoved(0)
                when(position){
                    PROFILE_TAB -> binding.fab.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_edit_user) })
                    CARS_TAB -> binding.fab.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.outline_add_24) })
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

/*        //When a user finish the car addition fragment
        val result = this.getNavigationResultLiveData<String>("test")
        //Do not convert to lambda. It will not create a new instance
        result?.observe(viewLifecycleOwner, object: Observer<String>{
            override fun onChanged(t: String?) {
                Timber.e("VALUE RECEIVED: $t")
            }
        })*/

//        Timber.e("AM AJUNS IN MY PROFILE!")


        viewModel.avatarUri.observe(viewLifecycleOwner, {
            //Load User photo
            Picasso.get().load(it).into(binding.ivPhoto)
        })
    }
}

