package com.catasoft.autoclub.ui.main.profilesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.ActivitySearchCarDetailsBinding
import com.catasoft.autoclub.ui.main.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
@AndroidEntryPoint
@FlowPreview
class SearchCarDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchCarDetailsBinding
    private var carId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCarDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Get the arguments
        val argBundle = intent.getBundleExtra(ProfileSearchFragment.SEARCH_CAR_BUNDLE_NAME)!!
        carId = argBundle.getString(ProfileSearchFragment.ARG_SEARCH_CAR_ID)

//        val transitionName: String = argBundle.getString(ProfileSearchFragment.ARGS_SEARCH_CAR_TRANSITION_NAME)!!
//        val carAvatarBitmap: Bitmap = argBundle.getParcelable(ProfileSearchFragment.ARGS_SEARCH_CAR_AVATAR_BITMAP)!!
/*        carId = argBundle.getString(ProfileSearchFragment.ARG_SEARCH_CAR_ID)
        Timber.e(carId)

        val frag = CarDetailsFragment()
        frag.arguments = bundleOf(ProfileSearchFragment.ARG_SEARCH_CAR_ID to carId)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.fragmentContainer, frag)
        fragmentTransaction.commit()*/

        //Pass the argument to the start destination
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val action = HomeFragmentDirections.actionHomeToCarDetailsFragment(carId, null, null)
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.home, true).build()
        navHostFragment.navController.navigate(action)

        binding.toolbar.setTitle("Search")


        binding.toolbar.onBackPressed {
            finish()
        }
    }
}