package com.catasoft.autoclub.ui.main.addcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentAddCarInfoBinding
import com.catasoft.autoclub.util.resetFeedback
import com.catasoft.autoclub.util.showSuccessEndIcon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnNext.setOnClickListener{
////            viewModel.createCar()
////            viewModel.validateInput()
//            viewModel.saveCar()
//        }


        binding.tfCarMake.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.validateNewCarMake(text.toString())
            binding.tfCarMake.resetFeedback()
        }


        binding.tfCarModel.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.validateNewCarModel(text.toString())
            binding.tfCarModel.resetFeedback()
        }

        binding.tfCarYear.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.validateNewCarYear(text.toString())
            binding.tfCarYear.resetFeedback()
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