package com.catasoft.autoclub.ui.main.feed

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentFeedBinding
import com.catasoft.autoclub.databinding.FragmentProfileBinding
import com.catasoft.autoclub.repository.CurrentUser
import com.catasoft.autoclub.ui.main.home.HomeFragmentDirections
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import timber.log.Timber

class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var binding: FragmentFeedBinding
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(requireContext())
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFeedBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.addMeet.setOnClickListener {
            //Start the new meet addition fragment
            val navController = findNavController()
            val action = FeedFragmentDirections.actionFeedToAddMeetActivity()
            navController.navigate(action)
        }

        return binding.root
    }

}