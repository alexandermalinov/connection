package com.connection.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.connection.R
import com.connection.databinding.*
import com.connection.ui.connectionchat.ConnectionChatPresenter
import com.connection.utils.DataBoundListAdapter
import com.connection.vo.message.*

private const val MESSAGE_BY_LOGGED_USER = 1
private const val MESSAGE_BY_SENDER_USER = 2
private const val IMAGE_MESSAGE_BY_LOGGED_USER = 3
private const val IMAGE_MESSAGE_BY_SENDER_USER = 4
private const val MESSAGE_DATE = 5

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
        MESSAGE_BY_SENDER_USER -> R.layout.list_item_message_sender_user
        IMAGE_MESSAGE_BY_LOGGED_USER -> R.layout.list_item_image_message_logged_user
        IMAGE_MESSAGE_BY_SENDER_USER -> R.layout.list_item_image_message_sender_user
        else -> R.layout.list_item_chat_date
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
            binding is ListItemImageMessageLoggedUserBinding && item is LoggedUserImageMessageUiModel -> {
                binding.model = item
            }
            binding is ListItemImageMessageSenderUserBinding && item is SenderUserImageMessageUiModel -> {
                binding.model = item
            }
            binding is ListItemChatDateBinding && item is MessageDateUiModel-> {
                binding.model = item
            }
        }
    }

    override fun getItemViewType(position: Int) = when (this.currentList[position]) {
        is LoggedUserMessageUiModel -> MESSAGE_BY_LOGGED_USER
        is SenderUserMessageUiModel -> MESSAGE_BY_SENDER_USER
        is LoggedUserImageMessageUiModel -> IMAGE_MESSAGE_BY_LOGGED_USER
        is SenderUserImageMessageUiModel -> IMAGE_MESSAGE_BY_SENDER_USER
        else -> MESSAGE_DATE
    }
}