package com.connection.ui.feed

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.remote.model.post.Like
import com.connection.data.api.remote.model.post.Posts
import com.connection.data.api.remote.model.post.toUiModels
import com.connection.data.api.remote.model.user.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseSendBirdViewModel
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.connectiontab.ConnectionsPresenter
import com.connection.ui.post.PostsPresenter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.FIRST_TEN_CHANNELS
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.POST
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.alltabs.FavouriteConnectionUiModel
import com.connection.vo.alltabs.toFavouriteConnections
import com.connection.vo.alltabs.toUiModel
import com.connection.vo.feed.FeedUiModel
import com.connection.vo.post.PostUiModel
import com.connection.vo.post.PostsUiModel
import com.connection.vo.post.toUiModel
import com.sendbird.android.GroupChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val chatTabRepository: ChatTabRepository
) : BaseSendBirdViewModel(chatTabRepository),
    FeedPresenter,
    PostsPresenter,
    FeedPostPresenter,
    ConnectionsPresenter {

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
            loadUser()
            loadChats()
            loadPosts()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadUser() {
        userRepository.getLoggedUser { user ->
            loggedUser = user
            _uiLiveData.value = FeedUiModel(profilePicture = user?.picture ?: EMPTY)
        }
    }

    private suspend fun loadPosts() {
        _uiLiveData.value?.loadingPosts = true
        postRepository.getUserPosts(
            onSuccess = { posts ->
                onReceiverPosts(posts)
                _uiLiveData.value?.loadingPosts = false
            },
            onFailure = { Timber.e("Error occurred fetching posts") }
        )
    }

    private suspend fun loadChats() {
        _uiLiveData.value?.loadingRecentChats = true
        chatTabRepository.fetchChannels(
            ConnectionStatus.CONNECTED,
            onSuccess = { channels ->
                onReceiveChats(channels)
                _uiLiveData.value?.loadingRecentChats = false
            },
            onFailure = {
                viewModelScope.launch {
                    connectUser(loggedUser)
                    loadChats()
                }
            })
    }

    private fun onReceiverPosts(posts: Posts) {
        if (posts.posts.isNotEmpty())
            loggedUser?.let { _postsLiveData.value = PostsUiModel(posts.toUiModels(it)) }
        else
            _uiLiveData.value = _uiLiveData.value?.copy(emptyPosts = true)
    }

    private fun onReceiveChats(channels: List<GroupChannel>) {
        if (channels.isNotEmpty())
            _connectionsUiLiveDate.value = channels
                .take(FIRST_TEN_CHANNELS)
                .toFavouriteConnections(loggedUser?.id ?: EMPTY)
        else
            _uiLiveData.value = _uiLiveData.value?.copy(emptyChats = true)
    }

    private fun getConnectionById(id: String) = _connectionsUiLiveDate.value
        ?.favouriteConnections
        ?.first { it.id == id }

    private fun updatePostLike(post: PostUiModel) {
        post.isLiked = !post.isLiked
        post.likesCount = if (post.isLiked)
            (post.likesCount.toInt() + 1).toString()
        else
            (post.likesCount.toInt() - 1).toString()
    }

    private fun navigateToChat(post: PostUiModel) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_feedFragment_to_connection_chat_fragment,
            bundleOf(HEADER_MODEL to post.toUiModel())
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun onPostClick(postId: String) {
        // TODO("Not yet implemented")
    }

    override fun onLikeClick(id: String) {
        viewModelScope.launch {
            _postsLiveData.value?.posts
                ?.find { post -> post.id == id }
                ?.let { post ->
                    updatePostLike(post)
                    postRepository.like(
                        id,
                        post.isLiked,
                        Like(
                            userId = loggedUser?.id ?: EMPTY,
                            userProfilePicture = loggedUser?.picture ?: EMPTY
                        )
                    )
                }
        }
    }

    override fun onCommentsClick(id: String) {
        _postsLiveData.value?.posts
            ?.find { post -> post.id == id }
            ?.let { post ->
                _navigationLiveData.value = NavigationGraph(
                    R.id.action_feedFragment_to_postCommentsFragment,
                    bundleOf(POST to post)
                )
            }
    }

    override fun onSaveClick(id: String) {
        // TODO("Not yet implemented")
    }

    override fun onConnectClick(post: PostUiModel) {
        navigateToChat(post)
    }

    override fun onUserClick(userId: String) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_feedFragment_to_userProfileFragment,
            bundleOf(USER_ID to userId)
        )
    }

    override fun onConnectionClick(id: String) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_feedFragment_to_connection_chat_fragment,
            bundleOf(HEADER_MODEL to getConnectionById(id)?.toUiModel())
        )
    }

    override fun onProfilePictureClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_feedFragment_to_profile_fragment)
    }

    override fun onCreatePostClick() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_feedFragment_to_imagePickerFragment,
            bundleOf(USER_ID to loggedUser?.id)
        )
    }

    override fun onSearchClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_feedFragment_to_searchFragment)
    }

    override fun onDiscoverClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_feedFragment_to_people_fragment)
    }
}