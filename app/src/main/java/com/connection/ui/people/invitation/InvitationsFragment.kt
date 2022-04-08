package com.connection.ui.people.invitation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentInvitationsBinding
import com.connection.databinding.FragmentNotConnectedPeopleBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.people.base.PeopleAdapter
import com.connection.ui.people.notconnected.NotConnectedPeopleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InvitationsFragment : BaseFragment<FragmentInvitationsBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: InvitationsViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPeopleRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_invitations

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initPeopleRecyclerView() {
        dataBinding.recyclerViewPeoples.apply {
            adapter = PeopleAdapter(viewModel)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        observeUiLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
            (dataBinding.recyclerViewPeoples.adapter as PeopleAdapter)
                .submitList(uiLiveData.peoples)
        }
    }
}