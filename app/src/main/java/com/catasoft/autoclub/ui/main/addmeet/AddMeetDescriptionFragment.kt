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

        val timer = object: CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                binding.outlinedTextField.error = null
                viewModel.liveValidationMessage.value = null
            }
        }

        binding.textInput.addTextChangedListener {
            val text = it.toString()
            viewModel.liveMeet.value?.description = text
            viewModel.liveMeet.value = viewModel.liveMeet.value
        }

        viewModel.liveValidationMessage.observe(viewLifecycleOwner, {
            timer.cancel()
            binding.outlinedTextField.error = it
            timer.start()
        })

        return rootView
    }
}