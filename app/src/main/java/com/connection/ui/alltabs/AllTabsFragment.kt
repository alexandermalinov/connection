package com.connection.ui.alltabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentAllTabsBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.connectiontab.ConnectionsAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllTabsFragment : BaseFragment<FragmentAllTabsBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: AllTabsViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun getLayoutId(): Int = R.layout.fragment_all_tabs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConnectionsRecyclerView()
        observeLiveData()
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initConnectionsRecyclerView() {
        dataBinding.recyclerViewConnections.apply {
            adapter = ConnectionsAdapter(viewModel)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeUiLiveData()
        observeConnectionsLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }

    private fun observeConnectionsLiveData() {
        viewModel.connectionUiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            (dataBinding.recyclerViewConnections.adapter as ConnectionsAdapter)
                .submitList(uiLiveData)
        }
    }
}