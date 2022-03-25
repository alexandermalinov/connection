package com.connection.ui.connectionchat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentConnectionChatBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.message.MessageAdapter
import com.connection.utils.common.Constants.POSITION_START
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectionChatFragment : BaseFragment<FragmentConnectionChatBinding>() {

    private val viewModel: ConnectionChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.presenter = viewModel
        initChatRecyclerView()
        observeLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun initChatRecyclerView() {
        dataBinding.recyclerViewChat.apply {
            adapter = MessageAdapter(viewModel)
            layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            scrollToPosition(POSITION_START)
        }
    }

    private fun observeLiveData() {
        observeUiLiveData()
        observeMessagesLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }

    private fun observeMessagesLiveData() {
        viewModel.messagesLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.apply {
                (recyclerViewChat.adapter as MessageAdapter)
                    .submitList(uiLiveData.messages)
                recyclerViewChat.smoothScrollToPosition(POSITION_START)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_connection_chat
}