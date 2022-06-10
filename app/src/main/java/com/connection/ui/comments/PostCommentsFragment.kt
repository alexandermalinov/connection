package com.connection.ui.comments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentPostCommentsBinding
import com.connection.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PostCommentsFragment : BaseFragment<FragmentPostCommentsBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: PostCommentsViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCommentsRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId() = R.layout.fragment_post_comments

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initCommentsRecyclerView() {
        dataBinding.recyclerViewComments.apply {
            adapter = CommentsAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeNavigation(viewModel.navigationLiveData)
        observeUiLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
            (dataBinding.recyclerViewComments.adapter as CommentsAdapter)
                .submitList(uiLiveData.comments)
        }
    }
}