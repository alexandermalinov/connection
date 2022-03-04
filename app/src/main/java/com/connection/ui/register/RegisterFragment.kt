package com.connection.ui.register

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connection.R
import com.connection.databinding.FragmentRegisterBinding
import com.connection.navigation.External
import com.connection.ui.base.BaseFragment
import com.connection.utils.ActivityResultHandler
import com.connection.utils.ActivityResultObserver
import com.connection.utils.SelectImageObserver
import com.connection.utils.common.grantReadUriPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(), ActivityResultHandler {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var selectImageObserver: SelectImageObserver

    override fun getLayoutId(): Int = R.layout.fragment_register

    override fun provideObserver(destination: External) = selectImageObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectImage()
        setObservers()
        observeLiveData()
    }

    private fun selectImage() {
        selectImageObserver = SelectImageObserver(requireActivity().activityResultRegistry) {
            it?.let { uri ->
                viewModel.setProfilePicture(uri)
                grantReadUriPermission(uri)
            }
        }
    }

    private fun setObservers() {
        with(viewLifecycleOwner.lifecycle) {
            addObserver(selectImageObserver)
        }
    }

    private fun observeLiveData() {
        dataBinding.presenter = viewModel
        observeUiLiveData()
        observeNavigation(viewModel.navigationLiveData)
    }

    private fun observeUiLiveData() {
        viewModel.uiLiveData.observe(viewLifecycleOwner) { uiLiveData ->
            dataBinding.model = uiLiveData
        }
    }
}