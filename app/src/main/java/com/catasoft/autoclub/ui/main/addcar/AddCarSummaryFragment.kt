package com.catasoft.autoclub.ui.main.addcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.catasoft.autoclub.databinding.FragmentAddCarSummaryBinding
import com.catasoft.autoclub.ui.BaseFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AndroidEntryPoint
class AddCarSummaryFragment : BaseFragment() {

    private lateinit var binding: FragmentAddCarSummaryBinding
    private val viewModel: AddCarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCarSummaryBinding.inflate(layoutInflater)
        val rootView = binding.root


        //lifecycle setters
        binding.lifecycleOwner = this

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.photoState.observe(viewLifecycleOwner, {
            if(it is AddCarViewModel.Companion.PhotoState.Valid){
                binding.imageView.setImageBitmap(it.photo)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.model = viewModel.carEntity
    }

}