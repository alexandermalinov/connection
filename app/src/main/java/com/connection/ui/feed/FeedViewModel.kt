package com.connection.ui.feed

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
import com.connection.ui.profile.ProfilePresenter
import com.connection.ui.profile.posts.PostsPresenter
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.feed.FeedUiModel
import com.connection.vo.post.PostsUiModel
import com.connection.vo.profile.ProfileUiModel
import com.connection.vo.profile.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseViewModel(), FeedPresenter, PostsPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<FeedUiModel>
        get() = _uiLiveData

    val postsLiveData: LiveData<PostsUiModel>
        get() = _postsLiveData

    private val _uiLiveData = MutableLiveData(FeedUiModel())
    private val _postsLiveData = MutableLiveData(PostsUiModel())
    private var loggedUserId = Constants.EMPTY

    init {
        viewModelScope.launch {
            loadUserData()
            userRepository.getUser(loggedUserId) { user ->
                _uiLiveData.value = _uiLiveData.value?.copy(userId = user?.id ?: EMPTY)
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
            id = _uiLiveData.value?.userId ?: EMPTY,
            onSuccess = { posts ->
                _postsLiveData.value = PostsUiModel(posts.toUiModels())
            },
            onFailure = {
                Timber.e("Error occurred fetching posts")
            }
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }
}