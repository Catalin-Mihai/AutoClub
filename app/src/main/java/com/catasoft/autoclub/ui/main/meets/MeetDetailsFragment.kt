package com.catasoft.autoclub.ui.main.meets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentMeetDetailsBinding
import com.catasoft.autoclub.model.meet.Meet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MeetDetailsFragment constructor(val meet: Meet): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMeetDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMeetDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.meet = meet

        // Inflate the layout for this fragment
        return binding.root
    }
}