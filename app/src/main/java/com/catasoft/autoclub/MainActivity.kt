package com.catasoft.autoclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.catasoft.autoclub.databinding.ActivityAddCarBinding
import com.catasoft.autoclub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
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