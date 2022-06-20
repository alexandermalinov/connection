package com.connection.ui.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.remote.response.post.Posts
import com.connection.data.remote.response.post.toUiModels
import com.connection.data.remote.response.user.UserData
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.ui.post.PostsPresenter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.post.PostsUiModel
import com.connection.vo.userprofile.UserProfileUiModel
import com.connection.vo.userprofile.toUserProfileUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(), UserProfilePresenter, PostsPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<UserProfileUiModel>
        get() = _uiLiveData

    val postsLiveData: LiveData<PostsUiModel>
        get() = _postsLiveData

    private val _uiLiveData = MutableLiveData(UserProfileUiModel())
    private val _postsLiveData = MutableLiveData(PostsUiModel())
    private var user: UserData? = null

    init {
        viewModelScope.launch {
            loadUserData()
            loadPosts()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadUserData() {
        userRepository.getUser(getUserIdByArgs()) { either ->
            either.fold({ error ->
                Timber.e("Error occurred while fetching user data: $error")
            }, { loggedUser ->
                user = loggedUser
                _uiLiveData.value = loggedUser?.toUserProfileUiModel(loggedUser.id)
            })
        }
    }

    private fun getUserIdByArgs() = savedStateHandle.get<String>(USER_ID) ?: EMPTY

    private suspend fun loadPosts() {
        postRepository.getUserPosts(
            id = getUserIdByArgs(),
            onSuccess = { posts ->
                onReceivePosts(posts)
                _uiLiveData.value = _uiLiveData.value?.copy(
                    postsCount = posts.posts.size.toString()
                )
            },
            onFailure = {
                Timber.e("Error occurred fetching posts")
            }
        )
    }

    private fun onReceivePosts(posts: Posts) {
        if (posts.posts.isNotEmpty())
            _postsLiveData.value = PostsUiModel(posts.toUiModels(user))
        else
            _uiLiveData.value = _uiLiveData.value?.copy(emptyPostsState = true)
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }

    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }
}