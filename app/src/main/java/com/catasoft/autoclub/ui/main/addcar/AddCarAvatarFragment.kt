package com.catasoft.autoclub.ui.main.addcar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentAddCarAvatarBinding
import com.catasoft.autoclub.databinding.FragmentAddCarInfoBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.FlowPreview
import java.lang.Exception

@FlowPreview
class AddCarAvatarFragment : Fragment() {

    //Use by activityviewmodels to share the same viewmodel across the pager's pages
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var binding: FragmentAddCarAvatarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddCarAvatarBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        binding.tvChangePhoto.setOnClickListener{
            pickAvatarImage()
            binding.tvChangePhoto.error = null
        }

        return binding.root
    }

    private fun pickAvatarImage() =
        ImagePicker.with(this)
            .crop(300f, 200f)	    			//Crop image(Optional), Check Customization for more option
            .compress(128)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1440, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                fileUri?.let {
                    Picasso.get().load(it).into(object: Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            //                        binding.btnNext.isClickable = true
                            viewModel.validatePhoto(bitmap)
                            binding.ivPhoto.setImageBitmap(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            //disable save button until the photo is loaded...
                            //                        binding.btnNext.isClickable = false
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

    //    private val pickImage =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                Picasso.get().load(uri).resize(400, 400).into(object: Target {
//                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
////                        binding.btnNext.isClickable = true
//                        viewModel.validatePhoto(bitmap)
//                        binding.ivPhoto.setImageBitmap(bitmap)
//                    }
//
//                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                    }
//
//                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                        //disable save button until the photo is loaded...
////                        binding.btnNext.isClickable = false
//                    }
//
//                })
//            }
//        }

}