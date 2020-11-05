package com.catasoft.autoclub.ui.main.accountsign

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.catasoft.autoclub.R
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AccountSignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.e("Gata, sunt in AccountSignActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_sign)

    }

}