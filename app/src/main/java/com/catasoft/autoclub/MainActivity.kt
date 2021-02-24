package com.catasoft.autoclub

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.catasoft.autoclub.databinding.ActivityMainBinding
import com.catasoft.autoclub.ui.main.accountsign.AccountSignActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val login = true

    private val startForLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode){
                RESULT_REGISTER_OK -> {
                    Snackbar.make(binding.root, resources.getText(R.string.register_success_registration), Snackbar.LENGTH_LONG).show()

                }

                RESULT_LOGIN_OK -> {
                    Snackbar.make(binding.root, resources.getString(R.string.login_success, "casca"), Snackbar.LENGTH_LONG).show()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root

        setContentView(rootView)

        //lifecycle setters
        binding.lifecycleOwner = this


        if(login){
            val intent = Intent(this, AccountSignActivity::class.java)
            Timber.e(intent.toString())
            startForLoginResult.launch(intent)
        }

    }

    companion object {
        const val RESULT_LOGIN_OK = 1
        const val RESULT_REGISTER_OK = 2
    }
}