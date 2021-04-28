package com.catasoft.autoclub.ui.main.car

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.catasoft.autoclub.databinding.FragmentCarDetailsBinding
import com.catasoft.autoclub.ui.BaseFragment
import com.squareup.picasso.Picasso
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

    init {
        Timber.e("Init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carId = args.carId
        Timber.e("On create %s", carId)

        if(carId != null)
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

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.carDetailsModelLive.observe(viewLifecycleOwner, {

            binding.recyclerView.adapter = CarDetailsPhotosAdapter(viewModel.carDetailsModelLive.value?.photosLinks, object: CarDetailsPhotosAdapter.CarGalleryListener{
                override fun imageClicked(imageView: ImageView, position: Int) {
                    openViewer(position, imageView)
                }
            })
        })

        return binding.root
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