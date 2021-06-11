package com.catasoft.autoclub.ui.main.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentRegisterMyProfileBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class RegisterMyProfileFragment : Fragment() {

    private lateinit var binding: FragmentRegisterMyProfileBinding
    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterMyProfileBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = binding.editTextTextPersonName
//        val name = nameEditText.text.toString().capitalize(Locale.ROOT)

        nameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.addUserName(text.toString().capitalize(Locale.ROOT))
        }

        viewModel.liveValidationMessage.observe(viewLifecycleOwner, {
            binding.editTextTextPersonName.error = it
        })
    }
}