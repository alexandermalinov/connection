package com.connection.ui.feed

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.post.toUiModels
import com.connection.data.api.model.user.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.connectiontab.ConnectionsPresenter
import com.connection.ui.profile.posts.PostsPresenter
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.FIRST_TEN_CHANNELS
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.alltabs.FavouriteConnectionUiModel
import com.connection.vo.alltabs.toFavouriteConnections
import com.connection.vo.alltabs.toUiModel
import com.connection.vo.feed.FeedUiModel
import com.connection.vo.post.PostsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val chatTabRepository: ChatTabRepository
) : BaseViewModel(), FeedPresenter, PostsPresenter, FeedPostPresenter, ConnectionsPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<FeedUiModel>
        get() = _uiLiveData

    val postsLiveData: LiveData<PostsUiModel>
        get() = _postsLiveData

    val connectionUiLiveData: LiveData<FavouriteConnectionUiModel>
        get() = _connectionsUiLiveDate

    private val _uiLiveData = MutableLiveData(FeedUiModel())
    private val _postsLiveData = MutableLiveData(PostsUiModel())
    private val _connectionsUiLiveDate = MutableLiveData(FavouriteConnectionUiModel())
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            userRepository.getLoggedUser { user ->
                loggedUser = user
                _uiLiveData.value = _uiLiveData.value?.copy(
                    profilePicture = user?.picture ?: EMPTY
                )
            }
            loadChats()
            loadPosts()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadPosts() {
        postRepository.getUserPosts(
            id = loggedUser?.id ?: EMPTY,
            onSuccess = { posts -> _postsLiveData.value = PostsUiModel(posts.toUiModels()) },
            onFailure = { Timber.e("Error occurred fetching posts") }
        )
    }

    private suspend fun loadChats() {
        chatTabRepository.fetchChannels(
            ConnectionStatus.CONNECTED,
            onSuccess = { channels ->
                _connectionsUiLiveDate.value = channels
                    .take(FIRST_TEN_CHANNELS.toInt())
                    .toFavouriteConnections(loggedUser?.id ?: EMPTY)
            },
            onFailure = {
                Timber.e("Failed to fetch channels")
            })
    }

    private fun getConnectionById(id: String) = _connectionsUiLiveDate.value
        ?.favouriteConnections
        ?.first { it.id == id }

    /* --------------------------------------------------------------------------------------------
 * Override
---------------------------------------------------------------------------------------------*/
    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }

    override fun onLikeClick(id: String) {
        _postsLiveData.value?.posts
            ?.find { post -> post.id == id }
            ?.let { post ->
                post.isLiked = !post.isLiked
            }
    }

    override fun onCommentsClick(id: String) {
        /*_postsLiveData.value?.posts
            ?.find { post -> post.id == id }
            ?.let { post ->
                _navigationLiveData.value = NavigationGraph(
                    R.id.action_feedFragment_to_postFragment,
                    bundleOf(POST_ID to post.id)
                )
            }*/
    }

    override fun onSaveClick(id: String) {
        // TODO("Not yet implemented")
    }

    override fun onConnectionClick(id: String) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_allMessagesFragment_to_connectionChatFragment,
            bundleOf(
                HEADER_MODEL to getConnectionById(id)?.toUiModel()
            )
        )
    }

    override fun onProfilePictureClick() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_feedFragment_to_profile_fragment
        )
    }

    override fun onCreatePostClick() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_feedFragment_to_imagePickerFragment,
            bundleOf(USER_ID to loggedUser?.id)
        )
    }
}