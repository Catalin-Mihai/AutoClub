package com.catasoft.autoclub.ui.main.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.databinding.FragmentFeedBinding
import com.catasoft.autoclub.model.meet.Meet
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(), MeetsListAdapter.MeetItemListener {

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var binding: FragmentFeedBinding
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var recyclerViewAdapter: MeetsListAdapter
    private var dataSet: ArrayList<Meet> = ArrayList()

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

    override fun onResume() {
        super.onResume()

        viewModel.getMeets()
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

        recyclerViewAdapter = MeetsListAdapter(dataSet, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = recyclerViewAdapter

        viewModel.meetsList.observe(viewLifecycleOwner, {
            dataSet.clear()
            dataSet.addAll(it)
            recyclerViewAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

    override fun onMeetClicked(meet: Meet) {
        Timber.e(meet.toString())
    }

}