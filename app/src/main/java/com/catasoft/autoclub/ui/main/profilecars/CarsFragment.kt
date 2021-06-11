package com.catasoft.autoclub.ui.main.profilecars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentCarsBinding
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.ui.main.home.ARG_USER_UID
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.google.android.material.transition.Hold
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CarsFragment : Fragment(), CarsListAdapter.CarItemListener {

    private lateinit var binding: FragmentCarsBinding
    private val viewModel: CarsViewModel by viewModels()
    private var userUid: String? = null

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
        userUid = arguments?.takeIf { it.containsKey(ARG_USER_UID) }?.getString(ARG_USER_UID)

        if(userUid == null) {
            //TODO: Make the snackbar functional. Now it's displayed pretty wrong
//            Snackbar.make(binding.tvDisplayName, resources.getText(R.string.generic_error), Snackbar.LENGTH_LONG).show()
            Toast.makeText(context, "Ceva nu a mers bine", Toast.LENGTH_LONG).show()
            return
        }

        val carsListAdapter = CarsListAdapter(viewModel.dataSource, userUid!!, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = carsListAdapter

        //Fetch the user cars
        viewModel.getUserCarsList(userUid!!)

        viewModel.carsList.observe(viewLifecycleOwner, {
            carsListAdapter.notifyDataSetChanged()
//            Timber.e(viewModel.dataSource.toString())
        })

        viewModel.liveCarDeleted.observe(viewLifecycleOwner, {
            it?.let {
                if(it){
                    Toast.makeText(requireContext(), "Autovehicul sters cu succes!", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(requireContext(), "Eroare la stergerea autovehiculului!", Toast.LENGTH_LONG).show()
                }
            }
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

    override fun onMoreOptionClickedOn(car: CarProfileModel, view: View) {
        Timber.e(car.toString())
//        viewModel.deleteCar(car.id!!)
        val popupMenu = popupMenu {
            section {
                item {
                    label = "Sterge"
                    icon = R.drawable.ic_delete //optional
                    callback = { //optional
                        viewModel.deleteCar(car.id!!, userUid!!)
                    }
                }
            }
        }

        popupMenu.show(requireContext(), view)
    }
}