package com.catasoft.autoclub.ui.main.addcar

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentAddCarInfoBinding
import com.catasoft.autoclub.util.resetFeedback
import com.catasoft.autoclub.util.setNavigationResult
import com.catasoft.autoclub.util.showSuccessEndIcon
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import java.lang.Exception

@FlowPreview
@AndroidEntryPoint
class AddCarInfoFragment : Fragment() {

    //Use by activityviewmodels to share the same viewmodel across the pager's pages
    private val viewModel: AddCarViewModel by activityViewModels()
    private lateinit var binding: FragmentAddCarInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddCarInfoBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        return binding.root
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                Picasso.get().load(uri).resize(400, 400).into(object: Target {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnNext.setOnClickListener{
////            viewModel.createCar()
////            viewModel.validateInput()
//            viewModel.saveCar()
//        }

        binding.tfCarMake.editText?.addTextChangedListener {
            viewModel.validateNewCarMake(it.toString())
            binding.tfCarMake.resetFeedback()
        }

        binding.tfCarModel.editText?.addTextChangedListener {
            viewModel.validateNewCarModel(it.toString())
            binding.tfCarModel.resetFeedback()
        }

        binding.tfCarYear.editText?.addTextChangedListener {
            viewModel.validateNewCarYear(it.toString())
            binding.tfCarYear.resetFeedback()
        }

        binding.tvChangePhoto.setOnClickListener{
            pickImage.launch("image/*")
            binding.tvChangePhoto.error = null
        }

//        viewModel.carCreated.observe(viewLifecycleOwner, {
//            if(it == true) {
//                this.setNavigationResult("STRING DE TEST!", "test")
//                findNavController().navigateUp()
//            }
//            else {
//                viewModel.checkIfExistsCarInfoInputs()
//                Snackbar.make(binding.root, "Eroare boss!", Snackbar.LENGTH_LONG).show()
//            }
//        })

        viewModel.carMakeState.observe(viewLifecycleOwner, {
            when(it) {
                AddCarViewModel.Companion.CarMakeState.Null ->
                    binding.tfCarMake.error = resources.getText(R.string.empty_input)
                is AddCarViewModel.Companion.CarMakeState.Valid -> {
                    binding.tfCarMake.showSuccessEndIcon()
                }
            }
        })

        viewModel.carModelState.observe(viewLifecycleOwner, {
            when(it){
                AddCarViewModel.Companion.CarModelState.Null ->
                    binding.tfCarModel.error = resources.getText(R.string.empty_input)
                is AddCarViewModel.Companion.CarModelState.Valid ->
                    binding.tfCarModel.showSuccessEndIcon()
            }
        })

        viewModel.carYearState.observe(viewLifecycleOwner, {
            when(it){
                AddCarViewModel.Companion.CarYearState.Null ->
                    binding.tfCarYear.error = resources.getText(R.string.empty_input)
                is AddCarViewModel.Companion.CarYearState.Valid ->
                    binding.tfCarYear.showSuccessEndIcon()
                is AddCarViewModel.Companion.CarYearState.NotInInterval ->
                    binding.tfCarYear.error = String.format(
                        resources.getString(R.string.add_car_car_year_not_in_interval),
                        viewModel.getYearInterval().first, viewModel.getYearInterval().second)
            }
        })

        viewModel.photoState.observe(viewLifecycleOwner, {
            when(it){
                is AddCarViewModel.Companion.PhotoState.Valid -> {
                }
                AddCarViewModel.Companion.PhotoState.Null ->
                    binding.tvChangePhoto.error = resources.getText(R.string.empty_input)
            }
        })
    }
}