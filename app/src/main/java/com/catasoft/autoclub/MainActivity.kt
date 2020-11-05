package com.catasoft.autoclub

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.catasoft.autoclub.ui.main.accountsign.AccountSignActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val login = true

    private val startForLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setContentView(R.layout.activity_main)
                Timber.e("Intent data: %s", it.data)
                val intent = it.data
                val firebaseId = intent?.getStringExtra("firebase_id")
                Timber.e("Firebase_id: $firebaseId")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("TIMBER TEST!")
        if(login){
            val intent = Intent(this, AccountSignActivity::class.java)
            Timber.e(intent.toString())
            startForLoginResult.launch(intent)
        }
//        else {
//            setContentView(R.layout.activity_main)
//        }

        Timber.e("Se continua executia din MainActivity")
    }
}