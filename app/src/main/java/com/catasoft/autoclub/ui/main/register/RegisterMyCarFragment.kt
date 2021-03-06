package com.catasoft.autoclub.ui.main.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentRegisterMycarBinding
import com.catasoft.autoclub.model.user.UserRegisterModel
import com.catasoft.autoclub.ui.BaseFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import java.util.*

@FlowPreview
@AndroidEntryPoint
class RegisterMyCarFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterMycarBinding
    private val viewModel: RegisterMyCarViewModel by viewModels()
    private var canGoToNextFragment = false
    private lateinit var lastNumberPlateInput: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterMycarBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    private fun getNumberPlateInputLayout() = binding.editTextCarLicense

    private fun formatInput(input: String): String {
        return input.toUpperCase(Locale.ROOT).trim()
    }

    private fun showEndIcon()
    {
        getNumberPlateInputLayout().apply {
            endIconMode = TextInputLayout.END_ICON_CUSTOM

            val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_check_circle_24px)
            drawable?.setTint(ResourcesCompat.getColor(resources, R.color.green_600, null))
            endIconDrawable = drawable
        }
    }

    private fun hideEndIcon()
    {
        getNumberPlateInputLayout().endIconMode = TextInputLayout.END_ICON_NONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val nextButton = binding.nextButton

            //Check if this number plate is already used

            nextButton.setOnClickListener{

                if(!canGoToNextFragment){

                    nextButton.error = resources.getString(R.string.register_car_unchecked_number_plate)
                    return@setOnClickListener
                }

                //Create the UserRegisterModel object here.
                //It will be passed from fragment to fragment until it reaches the final fragment.

                val userRegisterModel = UserRegisterModel()
                userRegisterModel.numberPlate = lastNumberPlateInput

                val navController = findNavController()
                val action =
                    RegisterMyCarFragmentDirections.actionRegisterCarToRegisterMyProfileFragment(
                        userRegisterModel
                    )
                navController.navigate(action)
            }

            //when input layout is ready
            getNumberPlateInputLayout().addOnEditTextAttachedListener {

                //hide the end icon
                hideEndIcon()

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
                    hideEndIcon()
                    nextButton.error = null
                }
            }

            viewModel.numberPlateAvailable.observe(viewLifecycleOwner, {

                when(it) {

                    is RegisterMyCarViewModel.Companion.NumberPlateState.NotUsed -> {

                        //We got a valid number plate, can go further
                        canGoToNextFragment = true

                        //Show the end icon confirmation
                        showEndIcon()
                    }

                    is RegisterMyCarViewModel.Companion.NumberPlateState.Fetching -> {

                    }

                    is RegisterMyCarViewModel.Companion.NumberPlateState.FetchError -> {
                        getNumberPlateInputLayout().error = resources.getString(R.string.fetch_error)
                    }

                    is RegisterMyCarViewModel.Companion.NumberPlateState.Used -> {
                        getNumberPlateInputLayout().error = resources.getString(R.string.register_car_already_used_number_plate)
                    }

                    is RegisterMyCarViewModel.Companion.NumberPlateState.InvalidFormat ->
                    {
                        getNumberPlateInputLayout().error = resources.getString(R.string.register_car_invalid_license)
                    }
                }
            })
    }
}