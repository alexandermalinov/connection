package com.connection.ui.profile

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.remote.model.post.Posts
import com.connection.data.api.remote.model.post.toUiModels
import com.connection.data.api.remote.model.user.UserData
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.post.PostsPresenter
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
    private var loggedUser: UserData? = null

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
        userRepository.getLoggedUser {
            loggedUser = it
            _uiLiveData.value = it?.toUiModel()
        }
    }

    private suspend fun loadPosts() {
        postRepository.getLoggedUserPosts(
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
            _postsLiveData.value = PostsUiModel(posts.toUiModels(loggedUser))
        else
            _uiLiveData.value = _uiLiveData.value?.copy(emptyPosts = true)
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
            bundleOf(USER_ID to loggedUser?.id)
        )
    }

    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }
}