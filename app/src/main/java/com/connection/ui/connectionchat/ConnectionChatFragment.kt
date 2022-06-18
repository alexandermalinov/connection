package com.connection.ui.connectionchat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.connection.R
import com.connection.databinding.FragmentConnectionChatBinding
import com.connection.navigation.External
import com.connection.ui.base.BasePermissionFragment
import com.connection.ui.gallery.GalleryAdapter
import com.connection.ui.message.MessageAdapter
import com.connection.utils.common.Constants.POSITION_START
import com.connection.utils.common.grantReadUriPermission
import com.connection.utils.image.ActivityResultHandler
import com.connection.utils.image.SelectImageObserver
import com.connection.utils.permissions.PermissionStateHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectionChatFragment : BasePermissionFragment<FragmentConnectionChatBinding>(),
    ActivityResultHandler {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: ConnectionChatViewModel by viewModels()
    private lateinit var selectImageObserver: SelectImageObserver

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChatRecyclerView()
        initGalleryRecyclerView()
        //selectImage()
        observeLiveData()
    }

    override fun getLayoutId(): Int = R.layout.fragment_connection_chat

    override fun provideObserver(destination: External) = selectImageObserver

    override fun providePermissionStateHandler() = viewModel

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initChatRecyclerView() {
        dataBinding.recyclerViewChat.apply {
            adapter = MessageAdapter(viewModel)
            layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }
            smoothScrollToPosition(POSITION_START)
        }
    }

    private fun initGalleryRecyclerView() {
        dataBinding.recyclerGallery.apply {
            adapter = GalleryAdapter(viewModel)
            layoutManager = GridLayoutManager(context, 4)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        addObservers()
        observeNavigation(viewModel.navigationLiveData)
        observeDialogLiveData(viewModel.dialogLiveData)
        observePermissionData(viewModel.permissionLiveData)
        observeUiLiveData()
        observeMessagesLiveData()
        observeGalleryLiveData()
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }

    private fun observeMessagesLiveData() {
        viewModel.messagesLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.apply {
                (recyclerViewChat.adapter as MessageAdapter).submitList(uiLiveData.messages)
                recyclerViewChat.smoothScrollToPosition(POSITION_START)
            }
        }
    }

    private fun observeGalleryLiveData() {
        viewModel.galleryLiveData.observe(viewLifecycleOwner) { galleryLiveData ->
            (dataBinding.recyclerGallery.adapter as GalleryAdapter).submitList(galleryLiveData)
        }
    }

    private fun selectImage() {
        selectImageObserver = SelectImageObserver(requireActivity().activityResultRegistry) {
            it?.let { uri ->
                viewModel.setImageMessage(uri)
                grantReadUriPermission(uri)
            }
        }
    }

    private fun addObservers() {
        with(viewLifecycleOwner.lifecycle) {
            addObserver(viewModel)
            //addObserver(selectImageObserver)
        }
    }
}