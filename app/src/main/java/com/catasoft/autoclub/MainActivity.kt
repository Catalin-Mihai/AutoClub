package com.catasoft.autoclub

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.catasoft.autoclub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        Timber.e("%s %s", navHostFragment.isHidden.toString(), navHostFragment.isVisible.toString() )

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)


        setContentView(view)
    }

    override fun onBackPressed() {

        super.onBackPressed()
        Timber.e("Back pressed from Main Activity")
    }

    override fun onResume() {
        super.onResume()
        Timber.e("Main activity on resume!")
    }
}