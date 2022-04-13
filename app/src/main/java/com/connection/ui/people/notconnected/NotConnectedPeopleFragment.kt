package com.connection.ui.people.notconnected

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentNotConnectedPeopleBinding
import com.connection.ui.base.BaseFragment
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
            adapter = NotConnectedAdapter(viewModel)
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
            (dataBinding.recyclerViewPeoples.adapter as NotConnectedAdapter)
                .submitList(uiLiveData.notConnections)
        }
    }
}