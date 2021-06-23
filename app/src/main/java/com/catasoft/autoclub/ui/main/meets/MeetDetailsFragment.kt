package com.catasoft.autoclub.ui.main.meets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentMeetDetailsBinding
import com.catasoft.autoclub.model.meet.Meet
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MeetDetailsFragment constructor(val meet: Meet): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMeetDetailsBinding
    private val viewModel: MeetDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMeetDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.meet = meet
        binding.viewModel = viewModel

        viewModel.getOwnerByUid(meet.ownerUid)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnOpenMaps.setOnClickListener {

            val gmmIntentUri =
//                Uri.parse("https://www.google.com/maps/place/?q=place_id:${meet.placeId}")
                Uri.parse("geo:0,0?q=${meet.placeLat},${meet.placeLong}(${meet.placeName})")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            mapIntent.resolveActivity(requireActivity().packageManager)?.let {
                startActivity(mapIntent)
            }
        }
    }
}