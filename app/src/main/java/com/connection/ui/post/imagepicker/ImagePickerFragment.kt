package com.connection.ui.post.imagepicker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.connection.R
import com.connection.databinding.FragmentImagePickerBinding
import com.connection.ui.base.BaseFragment
import com.connection.ui.gallery.GalleryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickerFragment : BaseFragment<FragmentImagePickerBinding>() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    private val viewModel: ImagePickerViewModel by viewModels()

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGalleryRecyclerView()
        observeLiveData()
    }

    override fun getLayoutId() = R.layout.fragment_image_picker

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun initGalleryRecyclerView() {
        dataBinding.recyclerGallery.apply {
            adapter = GalleryAdapter(viewModel)
            layoutManager = GridLayoutManager(context, 4)
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
            (dataBinding.recyclerGallery.adapter as GalleryAdapter).submitList(uiLiveData.galleryPictures)
        }
    }
}