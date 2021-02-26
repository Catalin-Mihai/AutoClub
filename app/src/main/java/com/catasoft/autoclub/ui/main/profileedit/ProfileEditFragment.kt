package com.catasoft.autoclub.ui.main.profileedit

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentProfileEditBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    private val viewModel: ProfileEditViewModel by viewModels()
    private lateinit var binding: FragmentProfileEditBinding
    private var validInput = false
    private var lastDisplayNameInput: String? = null
    private var lastFacebookProfileInput: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun getDisplayNameInputText(): String? {
        val editText = binding.tfDisplayName.editText?.text
        if(editText != null)
            return editText.toString()
        return null
    }

    private fun getFacebookProfileInputText(): String? {
        val editText = binding.tfFacebook.editText?.text
        if(editText != null)
            return editText.toString()
        return null
    }

    private fun errorForFacebookProfile(error: String?){
        binding.tfFacebook.error = error
    }

    private fun errorForDisplayName(error: String?){
        binding.tfDisplayName.error = error
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        TODO: validation for every field. Now I'm validating all fields on save press
        Can be done like the car number plate validation
        */

        binding.btnSave.setOnClickListener{

            viewModel.validateFields(getDisplayNameInputText(), getFacebookProfileInputText())
        }

        viewModel.displayNameState.observe(viewLifecycleOwner, {

            Timber.e(it.toString())
            when (it) {
                is ProfileEditViewModel.Companion.DisplayNameState.Valid -> {
                    viewModel.updateDisplayName(it.displayName)
                    errorForDisplayName(null)
                }

                ProfileEditViewModel.Companion.DisplayNameState.Unchanged -> {
                    //Do not display an error as it is already the user name
                    errorForDisplayName(null)
                }

                ProfileEditViewModel.Companion.DisplayNameState.Used -> {

                }

                ProfileEditViewModel.Companion.DisplayNameState.Null -> {
                    errorForDisplayName(resources.getString(R.string.empty_input))
                }

            }
        })

        viewModel.facebookProfileState.observe(viewLifecycleOwner, {

            Timber.e(it.toString())

            when(it){

                is ProfileEditViewModel.Companion.FacebookProfileState.Valid -> {
                    viewModel.updateFacebookProfile(it.facebookProfile)
                    errorForFacebookProfile(null)
                }

                ProfileEditViewModel.Companion.FacebookProfileState.Null -> {
                    errorForFacebookProfile(resources.getString(R.string.empty_input))
                }

                ProfileEditViewModel.Companion.FacebookProfileState.Unchanged -> {
                    errorForFacebookProfile(null)
                }

                ProfileEditViewModel.Companion.FacebookProfileState.Used -> {

                }

            }
        })

    }

}