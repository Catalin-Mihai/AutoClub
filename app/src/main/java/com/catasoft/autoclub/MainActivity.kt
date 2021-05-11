package com.catasoft.autoclub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.catasoft.autoclub.api.PlacesAPI
import com.catasoft.autoclub.api.PlacesAPIService
import com.catasoft.autoclub.databinding.ActivityMainBinding
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.security.auth.callback.Callback

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

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        setContentView(view)

        //Initialize Places
        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.google_api_key))

/*        val cal: Calendar = Calendar.getInstance()
        cal.set(2021, 4, 11, 13, 52)
        val milis = cal.timeInMillis
        Timber.e("%d", milis)
        cal.timeInMillis = milis + 555555555
        Timber.e("%s", cal.toString())*/


//        // Create a new PlacesClient instance
//        val placesClient = Places.createClient(this)
//        https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY
/*        val photoRef="CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU"
        val call = PlacesAPI().instance?.getPhoto(photoReference=photoRef, apiKey=resources.getString(R.string.google_api_key))
        call?.enqueue(object: retrofit2.Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Timber.e("response: $response")
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Timber.e("response: ${t.message}")
            }

        })*/
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