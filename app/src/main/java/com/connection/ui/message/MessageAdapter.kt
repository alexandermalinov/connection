package com.connection.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.ListItemMessageLoggedUserBinding
import com.connection.databinding.ListItemMessageSenderUserBinding
import com.connection.ui.connectionchat.ConnectionChatPresenter
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.message.LoggedUserMessageUiModel
import com.connection.vo.message.MessageListUiModel
import com.connection.vo.message.SenderUserMessageUiModel

private const val MESSAGE_BY_LOGGED_USER = 1
private const val MESSAGE_BY_SENDER_USER = 2

class MessageAdapter(val presenter: ConnectionChatPresenter) :
    DataBoundListAdapter<MessageListUiModel, ViewDataBinding>(
        object : DiffUtil.ItemCallback<MessageListUiModel>() {

            override fun areItemsTheSame(
                oldItem: MessageListUiModel,
                newItem: MessageListUiModel
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: MessageListUiModel,
                newItem: MessageListUiModel
            ) = oldItem == newItem
        }
    ) {

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun getLayout(viewType: Int) = when (viewType) {
        MESSAGE_BY_LOGGED_USER -> R.layout.list_item_message_logged_user
        else -> R.layout.list_item_message_sender_user
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun createBinding(
        parent: ViewGroup,
        viewType: Int
    ): ViewDataBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayout(viewType),
            parent,
            false
        )

    override fun bind(
        binding: ViewDataBinding,
        item: MessageListUiModel
    ) {
        when {
            binding is ListItemMessageLoggedUserBinding && item is LoggedUserMessageUiModel -> {
                binding.model = item
            }
            binding is ListItemMessageSenderUserBinding && item is SenderUserMessageUiModel -> {
                binding.model = item
            }
        }
    }

    override fun getItemViewType(position: Int) = when (this.currentList[position]) {
        is LoggedUserMessageUiModel -> MESSAGE_BY_LOGGED_USER
        else -> MESSAGE_BY_SENDER_USER
    }
}