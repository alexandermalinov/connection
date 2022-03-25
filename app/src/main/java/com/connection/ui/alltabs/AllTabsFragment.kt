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

@AndroidEntryPoint
class AllTabsFragment : BaseFragment<FragmentAllTabsBinding>() {

    private val viewModel: AllTabsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConnectionsRecyclerView()
        initFavouriteConnectionsRecyclerView()
        observeLiveData()
    }

    private fun initConnectionsRecyclerView() {
        dataBinding.recyclerViewConnections.apply {
            adapter = ConnectionsAdapter(viewModel)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initFavouriteConnectionsRecyclerView() {
        dataBinding.listFavouriteConnections.apply {
            adapter = FavouritesConnectionsAdapter()
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeUiLiveData()
        observeFavouriteConnectionsUiLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
            (dataBinding.recyclerViewConnections.adapter as ConnectionsAdapter)
                .submitList(uiLiveData.connections)
        }
    }

    private fun observeFavouriteConnectionsUiLiveData() {
        viewModel.favouriteConnectionsUiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            (dataBinding.listFavouriteConnections.adapter as FavouritesConnectionsAdapter)
                .submitList(uiLiveData.favouriteConnections)
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_all_tabs
}