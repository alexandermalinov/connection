package com.connection.ui.post.createpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connection.R
import com.connection.databinding.FragmentCreatePostBinding
import com.connection.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePostFragment : BaseFragment<FragmentCreatePostBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: CreatePostViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    override fun getLayoutId() = R.layout.fragment_create_post

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeNavigation(viewModel.navigationLiveData)
        observeUiLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }
}