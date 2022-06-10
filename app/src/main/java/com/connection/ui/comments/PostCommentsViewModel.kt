package com.connection.ui.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.post.Comment
import com.connection.data.api.model.post.toUiModel
import com.connection.data.api.model.user.UserData
import com.connection.data.repository.post.PostRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.POST
import com.connection.vo.comments.PostCommentListItemUiModel
import com.connection.vo.comments.PostCommentUiModel
import com.connection.vo.comments.toModel
import com.connection.vo.post.PostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostCommentsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(), PostCommentPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<PostCommentUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(PostCommentUiModel())
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            loadPost()
            loadUser()
            loadComments()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private fun loadPost() {
        savedStateHandle.get<PostUiModel>(POST)?.let {
            _uiLiveData.value = _uiLiveData.value?.copy(
                creatorPicture = it.creatorPicture,
                creatorUsername = it.creatorUsername,
                creatorId = it.creatorId,
                postId = it.id,
                postCreatedAt = it.createdAt,
                postDescription = it.description
            )
        }
    }

    private suspend fun loadUser() {
        userRepository.getLoggedUser { user ->
            loggedUser = user
        }
    }

    private suspend fun loadComments() {
        postRepository.getPostComments(
            postId = _uiLiveData.value?.postId ?: EMPTY,
            onSuccess = { posts -> onReceiverComments(posts) },
            onFailure = { Timber.e("Error occurred fetching posts") }
        )
    }

    private fun onReceiverComments(comments: List<Comment>) {
        if (comments.isNotEmpty())
            _uiLiveData.value = _uiLiveData.value?.copy(comments = comments.toUiModel())
        else
            _uiLiveData.value = _uiLiveData.value?.copy(emptyComments = true)
    }

    private fun getLoadedComments() = _uiLiveData.value?.comments ?: emptyList()

    private fun createComment() = _uiLiveData.value
        ?.let {
            PostCommentListItemUiModel(
                postId = it.postId,
                creatorId = loggedUser?.id ?: EMPTY,
                creatorPicture = loggedUser?.picture ?: EMPTY,
                creatorUsername = loggedUser?.username ?: EMPTY,
                comment = it.commentText
            )
        }
        ?: PostCommentListItemUiModel()

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun postComment() {
        viewModelScope.launch {
            createComment().let {
                _uiLiveData.value = _uiLiveData.value?.copy(
                    comments = listOf(it).plus(getLoadedComments())
                )
                postRepository.createComment(it.toModel())
            }
        }
    }
}