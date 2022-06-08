package com.connection.ui.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentFeedBinding
import com.connection.ui.alltabs.FavouritesConnectionsAdapter
import com.connection.ui.base.BaseFragment
import com.connection.ui.connectiontab.ConnectionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: FeedViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConnectionsRecyclerView()
        initPostsRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId() = R.layout.fragment_feed

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initPostsRecyclerView() {
        dataBinding.recyclerViewPosts.apply {
            adapter = PostsFeedAdapter(viewModel)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initConnectionsRecyclerView() {
        dataBinding.recyclerViewFavouriteConnections.apply {
            adapter = FavouritesConnectionsAdapter(viewModel)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeNavigation(viewModel.navigationLiveData)
        observeUiLiveData()
        observeConnectionsLiveData()
        observePostsLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }

    private fun observePostsLiveData() {
        viewModel.postsLiveData.observe(viewLifecycleOwner) { postsLiveData ->
            (dataBinding.recyclerViewPosts.adapter as PostsFeedAdapter).submitList(postsLiveData.posts)
        }
    }

    private fun observeConnectionsLiveData() {
        viewModel.connectionUiLiveData.observe(viewLifecycleOwner) { connectionsLiveData ->
            (dataBinding.recyclerViewFavouriteConnections.adapter as FavouritesConnectionsAdapter)
                .submitList(connectionsLiveData.favouriteConnections)
        }
    }
}