package com.catasoft.autoclub.ui.main.car

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentCarDetailsBinding
import com.catasoft.autoclub.databinding.FragmentProfileEditBinding
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val ARG_CAR = "car"

@AndroidEntryPoint
class CarDetailsFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var car: CarProfileModel? = null
    private lateinit var binding: FragmentCarDetailsBinding
    private val args: CarDetailsFragmentArgs by navArgs()

    init {
        Timber.e("Init")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val car = args.car
        Timber.e("On create %s", car)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.e("On create view!")

        binding = FragmentCarDetailsBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        return binding.root
    }

/*    companion object {
        @JvmStatic
        fun newInstance(car: Car) =
            CarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CAR, car)
                }
            }
    }*/
}