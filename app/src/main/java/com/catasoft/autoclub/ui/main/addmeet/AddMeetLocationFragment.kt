package com.catasoft.autoclub.ui.main.addmeet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentAddMeetLocationBinding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import timber.log.Timber

class AddMeetLocationFragment : Fragment() {

    private lateinit var binding: FragmentAddMeetLocationBinding
    private val viewModel: AddMeetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddMeetLocationBinding.inflate(layoutInflater)
        val rootView = binding.root

        //lifecycle setters
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.pickLocationBtn.setOnClickListener {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG, Place.Field.ADDRESS)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext())
            startActivityForResult(intent, AddMeetViewModel.AUTOCOMPLETE_REQUEST_CODE)
        }

        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AddMeetViewModel.AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Timber.e("Place: ${place.name}, ${place.id}, ${place.address} ${place.latLng?.latitude} ${place.latLng?.longitude}")

                        val endpoint = "https://maps.googleapis.com/maps/api/place/photo?"
                        val maxWidth = 400
                        val photoRef = place.photoMetadatas?.get(0)?.zza()
                        val uri = "${endpoint}maxwidth=${maxWidth}&photoreference=${photoRef}&key=${resources.getString(R.string.google_api_key)}"
                        Timber.e(uri)

                        viewModel.liveMeet.value!!.apply {
                            placeId = place.id
                            placeName = place.name
                            placeLat = place.latLng?.latitude
                            placeLong = place.latLng?.longitude
                            placeAddress = place.address
                            placePhotoLink = uri
                        }
                        viewModel.liveMeet.value = viewModel.liveMeet.value

                        Timber.e("attributions: ${place.photoMetadatas?.get(0)?.zza()}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.e(status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}