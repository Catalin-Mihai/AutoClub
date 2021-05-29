package com.catasoft.autoclub.ui.main.car

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.catasoft.autoclub.MainActivity
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentCarDetailsBinding
import com.catasoft.autoclub.model.car.CarPhotoModel
import com.catasoft.autoclub.ui.BaseFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.nambimobile.widgets.efab.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber


private const val ARG_CAR = "car"

@InternalCoroutinesApi
@AndroidEntryPoint
class CarDetailsFragment : BaseFragment(), CarDetailsPhotosAdapter.CarGalleryListener {
    // TODO: Rename and change types of parameters
    private var carId: String? = null
    private lateinit var binding: FragmentCarDetailsBinding
    private val viewModel: CarDetailsViewModel by viewModels()
    private val args: CarDetailsFragmentArgs by navArgs()
    private lateinit var viewer: StfalconImageViewer<CarPhotoModel>
    private lateinit var loadingSnackbar: Snackbar
    private var efabChildrenViewGroup: List<View>? = null
    private var carPhotoModels: ArrayList<CarPhotoModel> = ArrayList()
    private var overlayView: CarPhotoOverlayView? = null

    private val addPhotoBtn by lazy {
        createAddPhotoBtn()
    }
    private val addDescriptionBtn by lazy {
        createAddDescriptionBtn()
    }

    init {
        Timber.e("Init")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carId = args.carId
        Timber.e("On create %s", carId)

        if (carId == null)
            findNavController().navigateUp()

        viewModel.loadCarDetailsModel(carId!!)
    }

    private fun setupOverlayView(carPhotoModels: ArrayList<CarPhotoModel>, startPosition: Int) {
        overlayView = CarPhotoOverlayView(requireContext()).apply {

            update(carPhotoModels[startPosition])

            onDeleteClick = {

                val currentPosition = viewer.currentPosition()

                viewModel.deleteCarPhoto(carPhotoModels[currentPosition].carId!!, carPhotoModels[currentPosition].photoUri!!)

                carPhotoModels.removeAt(currentPosition)
                val photos = carPhotoModels.toMutableList()
                viewer.updateImages(photos)

                carPhotoModels.getOrNull(currentPosition)
                    ?.let { photo -> update(photo) }
            }
        }
    }

    private fun openViewer(startPosition: Int, target: ImageView) {

        setupOverlayView(carPhotoModels, startPosition)

        viewer = StfalconImageViewer.Builder(
            context,
            carPhotoModels
        ) { view, carModel ->
            Picasso.get().load(carModel.photoUri).into(view)
        }
            .withImageChangeListener { position ->
                overlayView?.update(carPhotoModels[position])
            }
            .withStartPosition(startPosition)
            .withTransitionFrom(target)
            .withOverlayView(overlayView)
            .show()
    }

    private fun createAddPhotoBtn(): FabOption{
        //add photo fab btn
        val addPhotoBtn = FabOption(requireContext(), Orientation.PORTRAIT)
        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.outline_photo_camera_24)
        drawable?.setTint(ResourcesCompat.getColor(resources, R.color.white, null))
        addPhotoBtn.fabOptionIcon = drawable
        addPhotoBtn.fabOptionColor = ResourcesCompat.getColor(resources, R.color.secondaryDarkColor, null)
        addPhotoBtn.label.labelText = "Incarca o fotografie"
        return addPhotoBtn
    }

    private fun createAddDescriptionBtn(): FabOption{
        //add description fab btn
        val addDescriptionBtn = FabOption(requireContext(), Orientation.PORTRAIT)
        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.outline_description_24)
        drawable?.setTint(ResourcesCompat.getColor(resources, R.color.white, null))
        addDescriptionBtn.fabOptionIcon = drawable
        addDescriptionBtn.fabOptionColor = ResourcesCompat.getColor(resources, R.color.primaryDarkColor, null)
        addDescriptionBtn.label.labelText = "Adauga o descriere"
        return addDescriptionBtn
    }

    private fun createFinalExpandableFAB(){

        //Main fab
        val expandableFab = ExpandableFab(requireContext(), Orientation.PORTRAIT)
        val params = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.WRAP_CONTENT,
            CoordinatorLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            marginEnd = 16
            bottomMargin = 16
        }
        expandableFab.layoutParams = params
        expandableFab.efabColor = ResourcesCompat.getColor(resources, R.color.primaryColor, null)

        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.outline_build_24)
        drawable?.setTint(ResourcesCompat.getColor(resources, R.color.white, null))
        expandableFab.efabIcon = drawable
        expandableFab.iconAnimationRotationDeg = 270f

        val fabsOverlay = Overlay(requireContext(), Orientation.PORTRAIT)

        binding.expandableFabLayout.addViews(expandableFab, addDescriptionBtn, addPhotoBtn, fabsOverlay)
    }

    override fun onPause() {
        //Some bugs with the library. This seems to fix them!
        binding.expandableFabLayout.removeAllViews()
        super.onPause()
    }

    override fun onResume() {
        createFinalExpandableFAB()
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        loadingSnackbar = Snackbar.make(binding.root, "Se salveaza...", Snackbar.LENGTH_INDEFINITE)

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        binding.recyclerView.adapter = CarDetailsPhotosAdapter(carPhotoModels, this)

        val activity = activity as MainActivity

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.toolbarLayout.setExpandedTitleColor(Color.WHITE)

        addPhotoBtn.setOnClickListener{
            pickImage()
        }

        addDescriptionBtn.setOnClickListener{
            context?.let { it1 ->
                MaterialDialog(it1).show {
                    title(R.string.add_car_car_description)

                    input(maxLength = 100) { _, text ->
                        Timber.e(text.toString())
                        carId?.let {
                            viewModel.saveDescription(text.toString(), it)
                        }
                    }
                    positiveButton(R.string.submit)
                    negativeButton(R.string.cancel)
                }.apply {
                    getInputField().apply {
                        inputType = (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        maxLines = 5
                    }
                }
            }
        }

        viewModel.carDetailsModelLive.observe(viewLifecycleOwner, {

            it.photosLinks?.let { photosLinks ->
                carPhotoModels.clear()

                val newDataCollection: ArrayList<CarPhotoModel> = photosLinks.map { uri ->
                    CarPhotoModel(
                        carId = this.carId,
                        photoUri = uri,
                        description = "A se pune data aici!!!"
                    )
                } as ArrayList<CarPhotoModel>


                carPhotoModels.addAll(newDataCollection)

                binding.recyclerView.adapter?.notifyDataSetChanged()
                loadingSnackbar.dismiss()

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.e("On create view!")

        binding = FragmentCarDetailsBinding.inflate(inflater)

        return binding.root
    }

    private fun pickImage() =
        ImagePicker.with(this)
            .crop(
                400f,
                400f
            )                    //Crop image(Optional), Check Customization for more option
            .compress(128)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                fileUri?.let {
                    Picasso.get().load(it).into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            //                        binding.btnNext.isClickable = true
                            if (bitmap == null) {
                                Toast.makeText(
                                    context,
                                    "Ceva nu a mers bine.. Incercati din nou!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                            viewModel.addPhoto(bitmap, carId!!)
//                            binding.ivCarPhoto.setImageBitmap(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                            Toast.makeText(
                                context,
                                "Ceva nu a mers bine.. Incercati din nou!",
                                Toast.LENGTH_SHORT
                            ).show()
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
                Toast.makeText(
                    context,
                    "Ceva nu a mers bine.. Incercati din nou!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun imageClicked(imageView: ImageView, position: Int) {
        openViewer(position, imageView)
    }

    override fun imageLongClicked(imageView: ImageView, position: Int) {
        Timber.e("LONG CLICK!")
    }
}