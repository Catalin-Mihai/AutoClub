package com.catasoft.autoclub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.catasoft.autoclub.ui.main.accountsign.AccountSignActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {
    private val context = this

    private val startForLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode){
                RESULT_REGISTER_OK -> {
//                    Snackbar.make(, resources.getText(R.string.register_success_registration), Snackbar.LENGTH_LONG).show()
                }

                RESULT_LOGIN_OK -> {
//                    Snackbar.make(binding.root, resources.getString(R.string.login_success, "casca"), Snackbar.LENGTH_LONG).show()
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            Timber.e(intent.toString())
            startMainActivity.launch(intent)
            login = false
        }

    private val startMainActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(login){
            val intent = Intent(this, AccountSignActivity::class.java)
            Timber.e(intent.toString())
            startForLoginResult.launch(intent)
            login = false
        }
        else
        {

        }

    }

    companion object {
        const val RESULT_LOGIN_OK = 1
        const val RESULT_REGISTER_OK = 2
        var login = true
    }
}