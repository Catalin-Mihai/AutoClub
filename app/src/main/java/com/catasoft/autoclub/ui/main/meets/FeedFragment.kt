package com.catasoft.autoclub.ui.main.meets

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import com.catasoft.autoclub.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.databinding.FragmentFeedBinding
import com.catasoft.autoclub.model.meet.Meet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(), MeetsListAdapter.MeetItemListener {

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var binding: FragmentFeedBinding
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var recyclerViewAdapter: MeetsListAdapter
    private var dataSet: ArrayList<Meet> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mostRecentSelectionText get() = resources.getString(R.string.meets_menu_most_recent)
    private val closestSelectionText get() = resources.getString(R.string.meets_menu_closest)

    private fun hasLocationPermission(): Boolean =
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
         /*{
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    private fun showClosestMeets(){

        fusedLocationClient.lastLocation .addOnSuccessListener { location: Location? ->
            if(location == null){
                Toast.makeText(requireContext(), "Nu am putut accesa locatia ta curenta!", Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.getClosetsMeets(location)
            }
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                showClosestMeets()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                val sortMenuComponent: AutoCompleteTextView? = binding.sortMenu.editText as? AutoCompleteTextView
                sortMenuComponent?.setText(mostRecentSelectionText)
                Toast.makeText(requireContext(),
                    "Cele mai apropiate meet-uri nu pot fi accesate fara acces la locatia dumneavoastra!",
                    Toast.LENGTH_SHORT).show()
            }
        }

    private fun showClosetsMeets() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                showClosestMeets()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
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

        //Sort menu
        val items = listOf(mostRecentSelectionText, closestSelectionText)
        val adapter = ArrayAdapter(requireContext(), R.layout.meets_sort_menu_item, items)
        Timber.e(items.toString())
        val sortMenuComponent: AutoCompleteTextView? = binding.sortMenu.editText as? AutoCompleteTextView
        sortMenuComponent?.setText(mostRecentSelectionText)
        sortMenuComponent?.setAdapter(adapter)

        sortMenuComponent?.doOnTextChanged { text, _, _, _ ->
            Timber.e(text.toString())
            if(text.toString() == mostRecentSelectionText){
                Timber.e("Most recent!")
                viewModel.getMeets()
            }
            else if(text.toString() == closestSelectionText){
                Timber.e("Closest!")
                showClosetsMeets()
            }
        }

        //Meets list
        recyclerViewAdapter = MeetsListAdapter(dataSet, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = recyclerViewAdapter

        viewModel.meetsList.observe(viewLifecycleOwner, {
            it?.let{ list ->
                dataSet.clear()
                dataSet.addAll(list)
                recyclerViewAdapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }


    override fun onMeetClicked(meet: Meet) {
        Timber.e(meet.toString())

        val meetBottomDialogFragment = MeetDetailsFragment(meet)
        activity?.supportFragmentManager?.let {
            meetBottomDialogFragment.show(it, meetBottomDialogFragment.tag)
        }

    }

}