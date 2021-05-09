package com.catasoft.autoclub.ui.main.addmeet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.databinding.FragmentAddMeetLocationBinding
import com.catasoft.autoclub.databinding.FragmentAddMeetSummaryBinding

class AddMeetSummaryFragment : Fragment() {

    private lateinit var binding: FragmentAddMeetSummaryBinding
    private val viewModel: AddMeetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddMeetSummaryBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return rootView
    }
}