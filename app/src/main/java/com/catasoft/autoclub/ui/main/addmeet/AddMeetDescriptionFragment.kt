package com.catasoft.autoclub.ui.main.addmeet

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.databinding.FragmentAddMeetDescriptionBinding
import com.catasoft.autoclub.databinding.FragmentAddMeetLocationBinding
import okhttp3.internal.notifyAll
import java.util.*

class AddMeetDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentAddMeetDescriptionBinding
    private val viewModel: AddMeetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddMeetDescriptionBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.textInput.addTextChangedListener {
            val text = it.toString().trim().capitalize(Locale.ROOT)
            viewModel.liveMeet.value?.description = text
            viewModel.liveMeet.value = viewModel.liveMeet.value
        }


        viewModel.liveValidationMessage.observe(viewLifecycleOwner, {
                binding.outlinedTextField.error = it
        })

        return rootView
    }
}