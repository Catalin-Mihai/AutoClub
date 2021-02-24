package com.catasoft.autoclub.ui.main.profilesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catasoft.autoclub.databinding.FragmentProfileSearchBinding
import com.catasoft.autoclub.ui.BaseFragment
import timber.log.Timber

class ProfileSearchFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSearchBinding.inflate(inflater)
        Timber.e("Am ajuns in profile search!")
        return binding.root
    }

}