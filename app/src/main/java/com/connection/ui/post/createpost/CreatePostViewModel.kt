package com.connection.ui.post.createpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.PICTURE
import com.connection.vo.post.createpost.CreatePostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository
) : BaseViewModel(), CreatePostPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
   ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<CreatePostUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(CreatePostUiModel())

    init {
        viewModelScope.launch {
            loadLoggedUser()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
   ---------------------------------------------------------------------------------------------*/
    private suspend fun loadLoggedUser() {
        userRepository.getLoggedUser { user ->
            _uiLiveData.value = CreatePostUiModel(
                creatorId = user?.id ?: EMPTY,
                creatorUsername = user?.username ?: EMPTY,
                creatorPicture = user?.picture ?: EMPTY,
                picture = getSelectedImage()
            )
        }
    }

    private fun getSelectedImage() = savedStateHandle.get(PICTURE) ?: EMPTY

    private fun createPost() {

    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSaveClick() {
        createPost()
    }
}