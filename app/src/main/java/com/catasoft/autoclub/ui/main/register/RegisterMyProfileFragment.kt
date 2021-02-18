package com.catasoft.autoclub.ui.main.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentRegisterMyProfileBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class RegisterMyProfileFragment : Fragment() {

    private val args: RegisterMyProfileFragmentArgs by navArgs()
    private lateinit var binding: FragmentRegisterMyProfileBinding
    private val viewModel: RegisterMyProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterMyProfileBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    private fun validateName(name: String): String?
    {
        if(name.length < 4 || name.length > 50)
            return String.format(resources.getString(R.string.register_profile_invalid_name), 4, 50)

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get the passed model from previous fragment
        val userRegisterModel = args.userRegisterModel
        val nameEditText = binding.editTextTextPersonName
        val nextButton = binding.nextButton

        nextButton.setOnClickListener{

            val name = nameEditText.text.toString().capitalize(Locale.ROOT)
            val errorMsg = validateName(name)
            var validInput = true

            if(errorMsg != null)
            {
                nameEditText.error = errorMsg
                validInput = false
            }

            if(validInput)
            {
                userRegisterModel.name = name
                Timber.e(userRegisterModel.toString())

                //Register the new user in the backend
                viewModel.registerUser(userRegisterModel)
            }
        }

        viewModel.registerState.observe(viewLifecycleOwner, {
            when(it){
                is RegisterState.Registered -> {
                    val navController = findNavController()
                    val action = RegisterMyProfileFragmentDirections.actionRegisterMyProfileFragmentToRegisterFinishFragment(userRegisterModel)
                    navController.navigate(action)
                }
                is RegisterState.Failed -> {
                    Snackbar.make(binding.root, resources.getText(R.string.register_fail_registration), Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}