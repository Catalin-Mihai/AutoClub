package com.catasoft.autoclub.ui.main.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catasoft.autoclub.R

class RegisterNewAccountFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterNewAccountFragment()
    }

    private lateinit var viewModel: RegisterNewAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_new_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterNewAccountViewModel::class.java)
        // TODO: Use the ViewModel
    }

}