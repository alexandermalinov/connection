package com.connection.ui.people.connected

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentConnectedPeopleBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.people.base.PeopleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectedPeopleFragment : BaseFragment<FragmentConnectedPeopleBinding>() {

    private val viewModel: ConnectedPeopleViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPeopleRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_connected_people

    private fun initPeopleRecyclerView() {
        dataBinding.recyclerViewPeoples.apply {
            adapter = PeopleAdapter(viewModel)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
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