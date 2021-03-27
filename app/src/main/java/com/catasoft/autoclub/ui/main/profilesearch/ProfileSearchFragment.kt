package com.catasoft.autoclub.ui.main.profilesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.databinding.FragmentProfileSearchBinding
import com.catasoft.autoclub.model.user.UserSearchModel
import com.catasoft.autoclub.ui.BaseFragment
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@FlowPreview
class ProfileSearchFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileSearchBinding
    private val viewModel: ProfileSearchViewModel by viewModels()

    private val searchList: ArrayList<UserSearchModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSearchBinding.inflate(inflater)
        Timber.e("Am ajuns in profile search!")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textField.editText?.addTextChangedListener {
            viewModel.pushInput(it.toString())
        }

        val searchListAdapter = SearchListAdapter(searchList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchListAdapter

        viewModel.usersLiveData.observe(viewLifecycleOwner, {
//            debug
//            for(user in it) {
//                Timber.e("%s", user)
//            }
            //Add received elements to our list
            searchList.clear()
            searchList.addAll(it)
            searchListAdapter.notifyDataSetChanged()
        })

//        binding.recyclerView.=
    }
}