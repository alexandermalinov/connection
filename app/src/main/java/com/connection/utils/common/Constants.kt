package com.connection.utils.common

object Constants {

    // GetStream Api Key
    const val GET_STREAM_API_KEY = "th6wez2ccn4x"
    const val GET_STREAM_TOKEN =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoic3F1YXJlLWNlbGwtOCJ9.F6q7JMOahzFNs4iAMJ6ysfkfZijdxYPIZ7wrISdEbN8"

    const val SENDBIRD_APP_ID = "E9E2372B-20B3-42CF-9B0A-0622BE89A901"

    // Common Constants
    const val EMPTY = ""
    const val INVALID_ID = 0L
    const val INVALID_RES = 0
    const val POSITION_START = 0

    // Delays
    const val SPLASH_SCREEN_DELAY = 2 * 1000L

    // External Storage Keys
    const val SELECT_IMAGE_KEY = "image_picker_key"
    const val IMAGE_TYPE = "image/*"

    // SaveStateHandle Keys
    const val USER_ID = "id"
    const val USERNAME = "username"
    const val USER_PICTURE = "user_picture"
    const val PICTURE = "picture"

    // Channel Types
    const val CHANNEL_TYPE_MESSAGING = "messaging"

    // Navigation Models
    const val HEADER_MODEL = "header_model"

    // User Extra data
    const val USER_EXTRA_DATA_USERNAME = "username"
    const val USER_EXTRA_DATA_PICTURE = "picture"
    const val USER_EXTRA_DATA_DESCRIPTION = "description"
    const val USER_EXTRA_DATA_CONNECTIONS = "connections"

    // Channel Extra data
    const val EXTRA_DATA_CHANNEL_PICTURE = "channel_picture"
    const val EXTRA_DATA_CHANNEL_NAME = "channel_name"
    const val EXTRA_DATA_CHANNEL_STATUS = "channel_status"

    // Connection Statuses
    const val CONNECTION_STATUS_CONNECTED = "connected"
    const val CONNECTION_STATUS_NOT_CONNECTED = "not connected"
    const val CONNECTION_STATUS_PENDING = "pending"

    // Creating channel data
    const val CHANNEL_MEMBERS = "members"
    const val CHANNEL_INVITES = "invites"
    const val CHANNEL_MESSAGE = "message"

    // Nested Fragments Navigation
    const val FRAGMENT_CONNECTED_PEOPLE = "fragment_connected_people"
    const val FRAGMENT_NOT_CONNECTED_PEOPLE = "fragment_not_connected_people"
    const val FRAGMENT_INVITATION_PEOPLE = "fragment_invitations"
    const val USER = "user"

    // People Screen Tabs Positions
    const val CONNECTED_PEOPLE_TAB_POSITION = 0
    const val NOT_CONNECTED_PEOPLE_TAB_POSITION = 1
    const val INVITED_PEOPLE_TAB_POSITION = 2

    // API paging
    const val PAGING_LIMIT = 15

    // Sendbird channel on receive listener
    const val CONNECTION_CHANNEL_LISTENER = "connection_channel_listener"

    // Posts
    const val POST_ID = "post_id"

    // Messages
    const val CONNECTION_REQUEST = "Connection Request"

    // Chats
    const val FIRST_TEN_CHANNELS = "10"

    // Posts
    const val POSTS = "posts"
    const val POST_LIKES = "likes"
}