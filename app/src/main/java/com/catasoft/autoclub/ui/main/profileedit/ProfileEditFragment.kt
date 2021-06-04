package com.catasoft.autoclub.ui.main.profileedit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentProfileEditBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Exception


@AndroidEntryPoint
class ProfileEditFragment : Fragment() {

    private val viewModel: ProfileEditViewModel by viewModels()
    private lateinit var binding: FragmentProfileEditBinding
    private var lastPhoto: Bitmap? = null

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

/*    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Picasso.get().load(uri).resize(400, 400).into(object: Target{
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        binding.btnSave.isClickable = true
                        lastPhoto = bitmap
                        binding.ivPhoto.setImageBitmap(bitmap)
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                        //disable save button until the photo is loaded...
                        binding.btnSave.isClickable = false
                    }

                })
            }
        }*/

    private fun pickAvatarImage() =
        ImagePicker.with(this)
            .crop(400f, 400f)	    			//Crop image(Optional), Check Customization for more option
            .compress(128)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1440 x 1080(Optional)
            .start()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                fileUri?.let {
                    Picasso.get().load(it).into(object: Target{
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            binding.btnSave.isClickable = true
                            lastPhoto = bitmap
                            binding.ivPhoto.setImageBitmap(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            //disable save button until the photo is loaded...
                            binding.btnSave.isClickable = false
                        }

                    })
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Ceva nu a mers bine.. Incercati din nou!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        TODO: validation for every field. Now I'm validating all fields on save press
        Can be done like the car number plate validation
        */

        binding.btnSave.setOnClickListener{

            viewModel.validateFields(getDisplayNameInputText(), getFacebookProfileInputText(), lastPhoto)
        }


        binding.tvChangePhoto.setOnClickListener{
            pickAvatarImage()
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

            when (it) {

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

        viewModel.photoState.observe(viewLifecycleOwner, {
            when(it) {
                ProfileEditViewModel.Companion.PhotoState.Error -> {
                    Snackbar.make(binding.root, resources.getText(R.string.edit_profile_photo_error), Snackbar.LENGTH_LONG)
                }
                is ProfileEditViewModel.Companion.PhotoState.Valid -> {
                    viewModel.updatePhoto(it.photo)
                }
            }
        })

    }

}