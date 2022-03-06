package com.connection.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connection.R
import com.connection.databinding.FragmentProfileBinding
import com.connection.ui.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
    }

    override fun getLayoutId() = R.layout.fragment_profile

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeUiLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }
}