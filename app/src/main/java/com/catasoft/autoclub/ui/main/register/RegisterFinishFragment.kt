package com.catasoft.autoclub.ui.main.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.StartActivity
import com.catasoft.autoclub.databinding.FragmentRegisterFinishBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class RegisterFinishFragment : Fragment() {

    private lateinit var binding: FragmentRegisterFinishBinding
    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterFinishBinding.inflate(layoutInflater)
        val rootView = binding.root

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registerState.observe(viewLifecycleOwner, {
            when(it){
                is RegisterState.Registered -> {
                    activity?.setResult(RegisterProfileActivity.RESULT_REGISTERED)
                    activity?.finish()
                }
                is RegisterState.Failed -> {
                    activity?.setResult(RegisterProfileActivity.RESULT_NOT_REGISTERED)
                    activity?.finish()
                }
            }
        })
    }

}