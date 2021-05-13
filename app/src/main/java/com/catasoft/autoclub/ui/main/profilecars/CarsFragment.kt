package com.catasoft.autoclub.ui.main.profilecars

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentCarsBinding
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.ui.main.home.ARG_USER_CARS
import com.catasoft.autoclub.ui.main.home.ARG_USER_UID
import com.catasoft.autoclub.ui.main.home.HomeFragmentDirections
import com.catasoft.autoclub.ui.main.profilesearch.SearchListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CarsFragment : Fragment(), CarsListAdapter.CarItemListener {

    private lateinit var binding: FragmentCarsBinding
    private val viewModel: CarsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarsBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userUid = arguments?.takeIf { it.containsKey(ARG_USER_UID) }?.getString(ARG_USER_UID)

        if(userUid == null) {
            //TODO: Make the snackbar functional. Now it's displayed pretty wrong
//            Snackbar.make(binding.tvDisplayName, resources.getText(R.string.generic_error), Snackbar.LENGTH_LONG).show()
            Toast.makeText(context, "Ceva nu a mers bine", Toast.LENGTH_LONG).show()
            return
        }

        val carsListAdapter = CarsListAdapter(viewModel.dataSource, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = carsListAdapter

        //Fetch the user cars
        viewModel.getUserCarsList(userUid)

        viewModel.carsList.observe(viewLifecycleOwner, {
            carsListAdapter.notifyDataSetChanged()
//            Timber.e(viewModel.dataSource.toString())
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Hold()
    }

    override fun onCarClicked(car: CarProfileModel, view: View) {

        val sharedImg: AppCompatImageView = view.findViewById(R.id.ivCarAvatar) as AppCompatImageView
        val extras = FragmentNavigatorExtras(sharedImg to sharedImg.transitionName)
        val args = Bundle()
        args.putString("carId", car.id)
        args.putString("transition_name", sharedImg.transitionName)
        args.putParcelable("avatar_bitmap", sharedImg.drawable.toBitmap())
        findNavController().navigate(R.id.action_home_to_carDetailsFragment, args, null, extras)

//        val navController = findNavController()
//        val action = HomeFragmentDirections.actionHomeToCarDetailsFragment(car.id)
//        navController.navigate(action)
    }
}