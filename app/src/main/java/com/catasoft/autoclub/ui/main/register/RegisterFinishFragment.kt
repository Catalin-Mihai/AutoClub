package com.catasoft.autoclub.ui.main.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.catasoft.autoclub.MainActivity
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentRegisterFinishBinding
import com.catasoft.autoclub.databinding.FragmentRegisterMyProfileBinding
import timber.log.Timber

class RegisterFinishFragment : Fragment() {

    private val args: RegisterFinishFragmentArgs by navArgs()
    private lateinit var binding: FragmentRegisterFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterFinishBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Populate views with the args received
        val userModel = args.userModel
        binding.tvCarPlate.text = userModel.numberPlate
        binding.tvProfileName.text = userModel.name


        //Finish the registration fragment on button click
        binding.btnFinish.setOnClickListener{
//            val returnIntent = Intent()
//            returnIntent.
            activity?.setResult(MainActivity.RESULT_REGISTER_OK)
            activity?.finish()

            Timber.e("OKKKK!!!")
        }
    }

}