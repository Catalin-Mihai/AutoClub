package com.catasoft.autoclub.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentHomeBinding
import com.catasoft.autoclub.databinding.FragmentProfileBinding
import com.catasoft.autoclub.model.getAvatarDownloadUrl
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.ui.main.home.ARG_USER_UID
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val argUid = arguments?.getString(ARG_USER_UID)

        if(argUid == null) {
            //TODO: Make the snackbar functional. Now it's displayed pretty wrong
            Snackbar.make(binding.tvDisplayName, resources.getText(R.string.generic_error), Snackbar.LENGTH_LONG).show()
        }
        else
        {
            viewModel.getUserByUid(argUid)

            viewModel.userLiveData.observe(viewLifecycleOwner, {
                //Load user info
                binding.tvDisplayName.text = it.name
                binding.tvJoinDate.text = it.joinDate
            })

        }
    }
}