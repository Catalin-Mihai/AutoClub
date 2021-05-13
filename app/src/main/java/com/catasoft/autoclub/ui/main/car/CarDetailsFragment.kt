package com.catasoft.autoclub.ui.main.car

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.catasoft.autoclub.MainActivity
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentCarDetailsBinding
import com.catasoft.autoclub.ui.BaseFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val ARG_CAR = "car"

@AndroidEntryPoint
class CarDetailsFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var carId: String? = null
    private lateinit var binding: FragmentCarDetailsBinding
    private val viewModel: CarDetailsViewModel by viewModels()
    private val args: CarDetailsFragmentArgs by navArgs()
    private lateinit var viewer: StfalconImageViewer<Uri?>
    private var photoDataSet: ArrayList<Uri> = ArrayList()
    private lateinit var loadingSnackbar: Snackbar

    init {
        Timber.e("Init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carId = args.carId
        Timber.e("On create %s", carId)

        if(carId == null)
            findNavController().navigateUp()

        viewModel.loadCarDetailsModel(carId!!)
    }

    private fun openViewer(startPosition: Int, target: ImageView) {

        viewer = StfalconImageViewer.Builder(context, viewModel.carDetailsModelLive.value?.photosLinks) { view, url ->
            Picasso.get().load(url).into(view)
        }
        .withStartPosition(startPosition)
        .withTransitionFrom(target)
        .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.e("On create view!")

        binding = FragmentCarDetailsBinding.inflate(inflater)

        val transitionName = args.transitionName
        binding.ivCarPhoto.transitionName = transitionName
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 500
        }
        exitTransition = MaterialFadeThrough().apply {
            duration = 2000
        }

        val avatarBitmap = args.avatarBitmap
        binding.ivCarPhoto.setImageBitmap(avatarBitmap)

        loadingSnackbar= Snackbar.make(binding.root, "Se salveaza...", Snackbar.LENGTH_INDEFINITE)

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.carDetailsModelLive.observe(viewLifecycleOwner, {

            binding.recyclerView.adapter = CarDetailsPhotosAdapter(photoDataSet, object: CarDetailsPhotosAdapter.CarGalleryListener{
                override fun imageClicked(imageView: ImageView, position: Int) {
                    openViewer(position, imageView)
                }
            })
        })

        val activity = activity as MainActivity
//        activity.setSupportActionBar(binding.toolbar)

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
//        binding.toolbarLayout.title = "Mithubishi lancer evo 3"

        binding.toolbarLayout.setExpandedTitleColor(Color.WHITE)

        binding.fab.setOnClickListener{
            pickImage()
        }

        viewModel.carDetailsModelLive.observe(viewLifecycleOwner, {
            it.photosLinks?.let { newData ->
                photoDataSet.clear()
                photoDataSet.addAll(newData)
                binding.recyclerView.adapter?.notifyDataSetChanged()
                loadingSnackbar.dismiss()
            }
        })

        return binding.root
    }

    private fun pickImage() =
        ImagePicker.with(this)
            .crop(400f, 400f)	    			//Crop image(Optional), Check Customization for more option
            .compress(128)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
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
                            if(bitmap == null){
                                Toast.makeText(context, "Ceva nu a mers bine.. Incercati din nou!", Toast.LENGTH_SHORT).show()
                                return
                            }
                            viewModel.addPhoto(bitmap, carId!!)
                            binding.ivCarPhoto.setImageBitmap(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            //disable save button until the photo is loaded...
                            //                        binding.btnNext.isClickable = false
                            loadingSnackbar.show()
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

/*    companion object {
        @JvmStatic
        fun newInstance(car: Car) =
            CarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CAR, car)
                }
            }
    }*/
}