package com.catasoft.autoclub.ui.main.profilesearch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catasoft.autoclub.R
import com.catasoft.autoclub.databinding.FragmentProfileSearchBinding
import com.catasoft.autoclub.model.user.UserSearchModel
import com.catasoft.autoclub.ui.BaseFragment
import com.catasoft.autoclub.ui.main.home.ARG_USER_UID
import com.catasoft.autoclub.ui.main.home.HomeFragment
import com.catasoft.autoclub.ui.main.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

@FlowPreview
@AndroidEntryPoint
class ProfileSearchFragment : BaseFragment(), SearchListAdapter.UserItemListener {

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

        val searchListAdapter = SearchListAdapter(searchList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchListAdapter

        binding.textField.editText?.addTextChangedListener {
            searchList.clear()
            searchListAdapter.notifyDataSetChanged()
            viewModel.pushInput(it.toString())
        }

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

    override fun onUserClicked(user: UserSearchModel) {
        Timber.e(user.toString())
        val bundle = bundleOf(ARG_USER_UID to user.uid)
//        findNavController().navigate(R.id.action_search_profile_to_searchInfoFragment, bundle)
        val intent = Intent(requireContext(), SearchProfileDetailsActivity::class.java).apply {
            putExtra(ARG_USER_UID, user.uid)
        }
        startActivity(intent)
    }
}