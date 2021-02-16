package com.catasoft.autoclub.ui.main.register

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentRegisterMycarBinding
import com.catasoft.autoclub.databinding.LoginFragmentBinding
import com.catasoft.autoclub.model.UserRegisterModel
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.ui.main.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class RegisterMyCarFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterMycarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterMycarBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    private fun validateLicense(license: String): String?
    {
        //A valid number looks like this: AG08BAW (exception B300XYZ that can have 3 digits on the middle and has only one letter for the county)
        //There are 6-7 characters

        Timber.e("CAPS: %s", license.toUpperCase(Locale.ROOT).trim())

        if (!license.trim().matches("""(?=.{6,7}$)^[A-Z]{1,2}[0-9]{1,3}[A-Z]{1,3}$""".toRegex()))
            return resources.getString(R.string.register_car_invalid_license)

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = binding.editTextCarLicense
        val nextButton = binding.nextButton

        nextButton.setOnClickListener{
            val license = editText.text.toString().toUpperCase(Locale.ROOT)

            val errorMsg = validateLicense(license)
            if(errorMsg != null)
            {
                editText.error = errorMsg
            }
            else
            {
                //Create the UserRegisterModel object here.
                //It will be passed from fragment to fragment until it reaches the final fragment.
                val userRegisterModel = UserRegisterModel()
                userRegisterModel.numberPlate = license

                val navController = findNavController()
                val action = RegisterMyCarFragmentDirections.actionRegisterCarToRegisterMyProfileFragment(userRegisterModel)
                navController.navigate(action)
            }
        }
    }
}