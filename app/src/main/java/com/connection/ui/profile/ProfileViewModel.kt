package com.connection.ui.profile

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.post.toUiModels
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.profile.posts.PostsPresenter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.post.PostsUiModel
import com.connection.vo.profile.ProfileUiModel
import com.connection.vo.profile.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel(), ProfilePresenter, PostsPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ProfileUiModel>
        get() = _uiLiveData

    val postsLiveData: LiveData<PostsUiModel>
        get() = _postsLiveData

    private val _uiLiveData = MutableLiveData(ProfileUiModel())
    private val _postsLiveData = MutableLiveData(PostsUiModel())
    private var loggedUserId = EMPTY

    init {
        viewModelScope.launch {
            loadUserData()
            userRepository.getUser(loggedUserId) { user ->
                _uiLiveData.value = user?.toUiModel()
            }
            loadPosts()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadUserData() {
        loggedUserId = userRepository.getLoggedUserId()
    }

    private suspend fun loadPosts() {
        postRepository.getUserPosts(
            id = _uiLiveData.value?.id ?: EMPTY,
            onSuccess = { posts ->
                _postsLiveData.value = PostsUiModel(posts.toUiModels(loggedUserId))
                _uiLiveData.value = _uiLiveData.value?.copy(
                    postsCount = posts.posts.size.toString()
                )
            },
            onFailure = {
                Timber.e("Error occurred fetching posts")
            }
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onLogoutClick() {
        viewModelScope.launch {
            userRepository.logoutUser {
                _navigationLiveData.value =
                    NavigationGraph(R.id.action_profileFragment_to_loginFragment)
            }
        }
    }

    override fun onCreatePostClick() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_profile_fragment_to_imagePickerFragment,
            bundleOf(USER_ID to loggedUserId)
        )
    }

    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }
}