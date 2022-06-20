package com.connection.ui.profile

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.remote.response.post.Post
import com.connection.data.remote.response.post.toUiModels
import com.connection.data.remote.response.user.UserData
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.menu.PopupMenuUiModel
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
        userRepository.getLoggedUser { either ->
            either.fold({ error ->
                Timber.e("Error occurred while fetching user data: $error")
            }, { user ->
                loggedUser = user
                _uiLiveData.value = user?.toUiModel()
            })
        }
    }

    private suspend fun loadPosts() {
        postRepository.getLoggedUserPosts() { either ->
            either.fold({ error ->
                Timber.e("Error occurred fetching posts: $error")
            }, { posts ->
                onReceivePosts(posts)
                _uiLiveData.value = _uiLiveData.value?.copy(
                    postsCount = posts.size.toString()
                )
            })
        }
    }

    private fun onReceivePosts(posts: List<Post>) {
        if (posts.isNotEmpty())
            _postsLiveData.value = PostsUiModel(posts.toUiModels(loggedUser))
        else
            _uiLiveData.value?.emptyPostsState = true
    }

    private fun navigateToCreatePost() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_profile_fragment_to_imagePickerFragment,
            bundleOf(USER_ID to loggedUser?.id)
        )
    }

    private fun logout() {
        viewModelScope.launch {
            userRepository.logoutUser { either ->
                either.fold({ error ->
                    Timber.e("Error occurred while log outing the user: $error")
                }, {
                    _navigationLiveData.value =
                        NavigationGraph(R.id.action_profileFragment_to_loginFragment)
                })
            }
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onCreatePostClick() {
        navigateToCreatePost()
    }

    override fun onMenuClick() {
        _popupMenuLiveData.value = PopupMenuUiModel(R.menu.menu_profile) {
            when (it) {
                R.id.action_create_post -> {
                    navigateToCreatePost()
                }
                R.id.action_logout -> {
                    logout()
                }
            }
        }
    }

    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }
}