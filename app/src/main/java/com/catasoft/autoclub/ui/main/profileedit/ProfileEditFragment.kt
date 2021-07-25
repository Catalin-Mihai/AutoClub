package com.catasoft.autoclub.ui.main.profileedit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.StartActivity
import com.catasoft.autoclub.databinding.FragmentProfileEditBinding
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.util.Constants.MAX_IMAGE_SIZE
import com.catasoft.autoclub.util.Constants.USER_AVATAR_HEIGHT
import com.catasoft.autoclub.util.Constants.USER_AVATAR_WIDTH
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


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

    private fun showSnackBar(message: String?){
        message?.let {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
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
            .crop(USER_AVATAR_WIDTH.toFloat(), USER_AVATAR_HEIGHT.toFloat())	    			//Crop image(Optional), Check Customization for more option
            .compress(MAX_IMAGE_SIZE)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(USER_AVATAR_WIDTH, USER_AVATAR_HEIGHT)	//Final image resolution will be less than 1440 x 1080(Optional)
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
                            lastPhoto = bitmap
                            binding.ivPhoto.setImageBitmap(bitmap)
                            binding.savePhotoBtn.visibility = View.VISIBLE
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
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

    private fun logOut(){
        val intent = Intent(requireContext(), StartActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        CurrentUser.invalidate()

        requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tfDisplayName.editText?.doOnTextChanged { text, _, _, _ ->
            if(text?.isNotEmpty() == true){
                binding.saveNameBtn.visibility = View.VISIBLE
            }
            else {
                binding.saveNameBtn.visibility = View.GONE
            }
        }

        binding.tfFacebook.editText?.doOnTextChanged { text, _, _, _ ->
            if(text?.isNotEmpty() == true){
                binding.saveFbBtn.visibility = View.VISIBLE
            }
            else {
                binding.saveFbBtn.visibility = View.GONE
            }
        }

        binding.tvChangePhoto.setOnClickListener{
            pickAvatarImage()
        }

        binding.saveNameBtn.setOnClickListener {
            val inputedName = binding.tfDisplayName.editText?.text.toString()
            viewModel.updateDisplayName(inputedName)
        }

        binding.saveFbBtn.setOnClickListener {
            val inputedFb = binding.tfFacebook.editText?.text.toString()
            viewModel.updateFacebookProfile(inputedFb)
        }

        binding.savePhotoBtn.setOnClickListener {
            viewModel.updatePhoto(lastPhoto)
        }

        binding.logoutBtn.setOnClickListener {
            logOut()
        }

        viewModel.displayNameState.observe(viewLifecycleOwner, {

            Timber.e(it.toString())
            when (it) {
                is ProfileEditViewModel.Companion.DisplayNameState.Saved -> {
                    showSnackBar("Noul nume a fost salvat!")
                }

                is ProfileEditViewModel.Companion.DisplayNameState.Unchanged -> {
                    //Do not display an error as it is already the user name
                    errorForDisplayName("Aveți deja acest nume!")
                }

                is ProfileEditViewModel.Companion.DisplayNameState.Used -> {

                }

                is ProfileEditViewModel.Companion.DisplayNameState.Null -> {
                    errorForDisplayName(resources.getString(R.string.empty_input))
                }

                is ProfileEditViewModel.Companion.DisplayNameState.Error -> {
                    errorForDisplayName("Eroare la salvarea numelui! Vă rugăm să reîncercați!")
                }

            }
        })

        viewModel.facebookProfileState.observe(viewLifecycleOwner, {

            Timber.e(it.toString())

            when (it) {

                is ProfileEditViewModel.Companion.FacebookProfileState.Saved -> {
                    showSnackBar("Noul profil de facebook a fost salvat!")
                }

                is ProfileEditViewModel.Companion.FacebookProfileState.Null -> {
                    errorForFacebookProfile(resources.getString(R.string.empty_input))
                }

                is ProfileEditViewModel.Companion.FacebookProfileState.Unchanged -> {
                    errorForFacebookProfile("Aveți deja asociat acest profil de facebook!")
                }

                is ProfileEditViewModel.Companion.FacebookProfileState.Used -> {

                }

                is ProfileEditViewModel.Companion.FacebookProfileState.Error -> {
                    errorForFacebookProfile("Eroare la salvarea profilului de facebook! Vă rugăm să reîncercați!")
                }

            }
        })

        viewModel.photoState.observe(viewLifecycleOwner, {
            when(it) {
                ProfileEditViewModel.Companion.PhotoState.Error -> {
                    showSnackBar(resources.getText(R.string.edit_profile_photo_error).toString())
                }
                is ProfileEditViewModel.Companion.PhotoState.Saved -> {
                    showSnackBar("Noua fotografie a fost salvată!")
                }
            }
        })

    }

}