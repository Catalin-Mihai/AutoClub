package com.catasoft.autoclub.ui.main.addcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentAddCarNumberPlateBinding
import com.catasoft.autoclub.databinding.FragmentRegisterMycarBinding
import com.catasoft.autoclub.model.user.UserRegisterModel
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.util.hideEndIcon
import com.catasoft.autoclub.util.showSuccessEndIcon
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import java.util.*

@FlowPreview
@AndroidEntryPoint
class AddCarNumberPlateFragment : BaseFragment() {

    private lateinit var binding: FragmentAddCarNumberPlateBinding
    private val viewModel: AddCarViewModel by activityViewModels()
    private var canGoToNextFragment = false
    private lateinit var lastNumberPlateInput: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCarNumberPlateBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    private fun getNumberPlateInputLayout() = binding.editTextCarLicense

    private fun formatInput(input: String): String {
        return input.toUpperCase(Locale.ROOT).trim()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            //when input layout is ready
            getNumberPlateInputLayout().addOnEditTextAttachedListener {

                //hide the end icon
                it.hideEndIcon()

                //listen for the user input
                it.editText?.addTextChangedListener { editText ->
                    //let only executes when editText is not null
                    editText?.toString()?.let { input ->
                        lastNumberPlateInput = formatInput(input)
                        viewModel.newCheckRequestFromView(lastNumberPlateInput)
                    }

                    //block user from going further until the check is done
                    canGoToNextFragment = false

                    //reset the input layout + button errors
                    getNumberPlateInputLayout().error = null
                    getNumberPlateInputLayout().hideEndIcon()
                }
            }

            viewModel.numberPlateAvailable.observe(viewLifecycleOwner, {

                when(it) {

                    is AddCarViewModel.Companion.NumberPlateState.NotUsed -> {

                        //We got a valid number plate, can go further
                        canGoToNextFragment = true

                        //Show the end icon confirmation
                        getNumberPlateInputLayout().showSuccessEndIcon()
                    }

                    is AddCarViewModel.Companion.NumberPlateState.Fetching -> {

                    }

                    is AddCarViewModel.Companion.NumberPlateState.FetchError -> {
                        getNumberPlateInputLayout().error = resources.getString(R.string.fetch_error)
                    }

                    is AddCarViewModel.Companion.NumberPlateState.Used -> {
                        getNumberPlateInputLayout().error = resources.getString(R.string.register_car_already_used_number_plate)
                    }

                    is AddCarViewModel.Companion.NumberPlateState.InvalidFormat ->
                    {
                        getNumberPlateInputLayout().error = resources.getString(R.string.register_car_invalid_license)
                    }
                }
            })
    }
}