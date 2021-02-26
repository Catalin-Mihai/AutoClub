package com.catasoft.autoclub.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.catasoft.autoclub.databinding.FragmentCarBinding


class CarFragment : Fragment() {

    private lateinit var binding: FragmentCarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarBinding.inflate(inflater)

        //lifecycle setters
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            binding.textView2.text = getInt(ARG_OBJECT).toString()
//        }
    }
}