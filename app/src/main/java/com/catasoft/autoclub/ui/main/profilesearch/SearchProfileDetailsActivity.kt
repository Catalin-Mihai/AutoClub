package com.catasoft.autoclub.ui.main.profilesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.ActivityAddMeetBinding
import com.catasoft.autoclub.databinding.ActivitySearchProfileDetailsBinding
import com.catasoft.autoclub.ui.main.home.ARG_USER_UID
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchProfileDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchProfileDetailsBinding
    private var userUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProfileDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userUid = intent.getStringExtra(ARG_USER_UID)
        Timber.e(userUid)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navHostFragment.navController.setGraph(R.navigation.main_graph, bundleOf(ARG_USER_UID to userUid))

        Timber.e("Nav: ")
    }
}