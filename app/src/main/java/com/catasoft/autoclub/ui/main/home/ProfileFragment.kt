package com.catasoft.autoclub.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catasoft.autoclub.databinding.ProfileFragmentBinding
import com.catasoft.autoclub.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

class ProfileFragment: BaseFragment(){

    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileFragmentBinding.inflate(inflater)
        return binding.root
    }
}

