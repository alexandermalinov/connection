package com.connection.ui.userprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.connection.R
import com.connection.databinding.FragmentUserProfileBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.post.PostsImageAdapter
import com.connection.utils.common.removeTransparentStatusBar
import com.connection.utils.common.setStatusBarTransparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: UserProfileViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPostsRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId() = R.layout.fragment_user_profile

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initPostsRecyclerView() {
        dataBinding.recyclerViewPosts.apply {
            adapter = PostsImageAdapter(viewModel)
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeNavigation(viewModel.navigationLiveData)
        observeUiLiveData()
        observePostsLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }

    private fun observePostsLiveData() {
        viewModel.postsLiveData.observe(viewLifecycleOwner) { postsLiveData ->
            (dataBinding.recyclerViewPosts.adapter as PostsImageAdapter).submitList(postsLiveData.posts)
        }
    }
}