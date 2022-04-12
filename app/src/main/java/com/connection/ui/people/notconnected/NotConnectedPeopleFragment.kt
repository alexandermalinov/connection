package com.connection.ui.people.notconnected

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentConnectedPeopleBinding
import com.connection.databinding.FragmentNotConnectedPeopleBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.people.base.PeopleAdapter
import com.connection.ui.people.connected.ConnectedPeopleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotConnectedPeopleFragment : BaseFragment<FragmentNotConnectedPeopleBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: NotConnectedPeopleViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPeopleRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_not_connected_people

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