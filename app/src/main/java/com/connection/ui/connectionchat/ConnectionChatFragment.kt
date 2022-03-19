package com.connection.ui.connectionchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.connection.R
import com.connection.databinding.FragmentConnectionChatBinding
import com.connection.databinding.FragmentLoginBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectionChatFragment : BaseFragment<FragmentConnectionChatBinding>() {

    private val viewModel: ConnectionChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.presenter = viewModel
        observeUiLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiModel ->
            dataBinding.model = uiModel
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_connection_chat
}