package com.connection.ui.post.createpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.PICTURE
import com.connection.vo.post.PostUiModel
import com.connection.vo.post.toPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel(), CreatePostPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
   ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<PostUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(PostUiModel())

    init {
        viewModelScope.launch {
            loadLoggedUser()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
   ---------------------------------------------------------------------------------------------*/
    private suspend fun loadLoggedUser() {
        userRepository.getLoggedUser { either ->
            either.fold({ error ->
                Timber.e("Error occurred while fetching user data: $error")
            }, { user ->
                user?.apply {
                    _uiLiveData.value = PostUiModel(
                        creatorId = id,
                        creatorUsername = username,
                        creatorPicture = picture,
                        picture = getSelectedImage()
                    )
                }
            })
        }
    }

    private fun getSelectedImage() = savedStateHandle.get(PICTURE) ?: EMPTY

    private suspend fun createPost() {
        _uiLiveData.value?.let {
            postRepository.savePostPicture(it.picture) { picture ->
                postRepository.createPost(it.toPost(picture))
            }
        }
    }

    private fun navigateToProfile() {
        _navigationLiveData.value = NavigationGraph(
            R.id.profile_fragment
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSaveClick() {
        viewModelScope.launch {
            createPost()
            navigateToProfile()
        }
    }
}