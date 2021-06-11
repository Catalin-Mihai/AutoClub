package com.catasoft.autoclub.ui.main.profilesearch

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.databinding.FragmentProfileSearchBinding
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.model.user.UserSearchModel
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.ui.main.home.ARG_USER_UID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@FlowPreview
@AndroidEntryPoint
class ProfileSearchFragment : BaseFragment(), UserSearchListAdapter.UserItemListener,
    CarsSearchListAdapter.CarItemListener {

    private lateinit var binding: FragmentProfileSearchBinding
    private val viewModel: ProfileSearchViewModel by viewModels()
    private val userSearchList: ArrayList<UserSearchModel> = ArrayList()
    private var searchMode: Int = USER_NAME_SEARCH

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSearchBinding.inflate(inflater)
        Timber.e("Am ajuns in profile search!")
        return binding.root
    }

    private fun getSearchMode(): Int {
        return searchMode
    }

    private fun setUserSearchAdapter(dataSet: List<UserSearchModel>){
        val searchListAdapter = UserSearchListAdapter(dataSet, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchListAdapter
    }

    private fun setCarsSearchAdapter(dataSet: List<Car>){
        val searchListAdapter = CarsSearchListAdapter(dataSet, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchListAdapter
    }

    private fun removeAdapter(){
        binding.recyclerView.adapter = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textField.editText?.addTextChangedListener {
            removeAdapter()
            viewModel.pushInput(it.toString().toUpperCase(), getSearchMode())
        }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            Timber.e(checkedId.toString())
            binding.textField.editText?.text?.clear()
            when(checkedId){
                binding.chipUserName.id -> {
                    searchMode = USER_NAME_SEARCH
                    binding.textField.isEnabled = true
                    binding.textField.hint = binding.chipUserName.text
                }
                binding.chipCarPlate.id -> {
                    searchMode = NUMBER_PLATE_SEARCH
                    binding.textField.isEnabled = true
                    binding.textField.hint = binding.chipCarPlate.text
                }
                -1 ->{
                    binding.textField.isEnabled = false
                    searchMode = NONE_SEARCH
                }
            }

            removeAdapter()
        }


        // By default the first mode is user name seach
        /*binding.chipUserName.isSelected = true
        binding.chipCarPlate.isSelected = false*/

        viewModel.usersLiveData.observe(viewLifecycleOwner, {
            //Add received elements to our list
            setUserSearchAdapter(it)
        })

        viewModel.carsLiveData.observe(viewLifecycleOwner, {
            setCarsSearchAdapter(it)
        })
    }

    override fun onUserClicked(user: UserSearchModel) {
        Timber.e(user.toString())
        val intent = Intent(requireContext(), SearchProfileDetailsActivity::class.java).apply {
            putExtra(ARG_USER_UID, user.uid)
        }
        startActivity(intent)
    }

    companion object{
        const val USER_NAME_SEARCH = 1
        const val NUMBER_PLATE_SEARCH = 2
        const val NONE_SEARCH = 3
    }

    override fun onCarClicked(car: Car) {
        Timber.e(car.toString())
    }
}