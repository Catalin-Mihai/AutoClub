package com.catasoft.autoclub

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.main.accountsign.AccountSignActivity
import com.catasoft.autoclub.ui.main.register.RegisterProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class StartActivity : AppCompatActivity() {

    private val startForLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode){
                RESULT_REGISTER_OK -> {
                    Toast.makeText(this, resources.getText(R.string.register_success_registration), Toast.LENGTH_LONG).show()
                    CurrentUser.initiate{
                        goToMainActivity()
                    }
                }

                RESULT_REGISTER_BAD -> {
                    Toast.makeText(this, resources.getText(R.string.register_fail_registration), Toast.LENGTH_LONG).show()
                }

                RESULT_LOGIN_OK -> {
                    CurrentUser.initiate{
                        if(CurrentUser.getEntity().uid == null){
                            //User authenticated but not registered in database
                            //Send the user to the register panel again
                            goToProfileRegister()
                        }
                        else {
                            Toast.makeText(this, resources.getString(R.string.login_success, CurrentUser.getEntity().name), Toast.LENGTH_LONG).show()
                            goToMainActivity()
                        }
                    }
                }
                RESULT_CANCELED -> {
                    //Back press
                    //Shut down the whole app by finishing this activity
                    finish()
                }
            }
            login = false
        }

    private fun goToProfileRegister(){
        val intent = Intent(this, RegisterProfileActivity::class.java)
        Timber.e(intent.toString())
        startProfileRegisterForResult.launch(intent)
    }

    private val startProfileRegisterForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when(it.resultCode){
                RegisterProfileActivity.RESULT_REGISTERED -> {
                    CurrentUser.initiate{
                        goToMainActivity()
                    }
                }
                RegisterProfileActivity.RESULT_NOT_REGISTERED -> {
                    Toast.makeText(this, resources.getText(R.string.register_fail_registration), Toast.LENGTH_LONG).show()
                }
            }
        }

    private fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        //Set these flags to start a fresh task. Back pressing will work correctly. The last back press will close the app
        //instead of returning to the StartActivity
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        Timber.e(intent.toString())
        startMainActivity.launch(intent)
        finish()
    }

    private val startMainActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}

    private fun gotoAccountSignActivity(){
        val intent = Intent(this, AccountSignActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        Timber.e(intent.toString())
        startForLoginResult.launch(intent)
    }

    private fun checkLoginStatus(){
//        if(login){
            Timber.e(login.toString())
            gotoAccountSignActivity()
            login = false
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.e("On create")
        checkLoginStatus()
    }

    override fun onResume() {
        super.onResume()

        Timber.e("ON RESUME ACTIVITY START")
//        login = true
//        checkLoginStatus()
    }

    companion object {
        const val RESULT_LOGIN_OK = 1
        const val RESULT_REGISTER_OK = 2
        const val RESULT_REGISTER_BAD = 3
        var login = true
    }
}