package com.connection.utils.common

object Constants {

    // Send Bird
    const val SENDBIRD_APP_ID = "E9E2372B-20B3-42CF-9B0A-0622BE89A901"

    // Common Constants
    const val EMPTY = ""
    const val INVALID_ID = 0L
    const val DEFAULT_ID = 0L
    const val INVALID_RES = 0
    const val POSITION_START = 0
    const val ZERO = "0"

    // Database
    const val DATABASE_NAME="connection_database"
    const val SEARCH_HISTORY = "search_history"
    const val SEARCH_BODY = "search_body"

    // Delays
    const val SPLASH_SCREEN_DELAY = 1000L

    // External Storage Keys
    const val SELECT_IMAGE_KEY = "image_picker_key"
    const val IMAGE_TYPE = "image/*"

    // SaveStateHandle Keys
    const val USER_ID = "id"
    const val USERNAME = "username"
    const val USER_PICTURE = "user_picture"
    const val PICTURE = "picture"

    // Navigation Models
    const val HEADER_MODEL = "header_model"

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

    // Messages
    const val CONNECTION_REQUEST = "Connection Request"

    // Chats
    const val FIRST_TEN_CHANNELS = 10

    // DataBase
    const val USERS = "users"
    const val POSTS = "posts"
    const val POST_LIKES = "likes"
    const val POST = "post"
    const val POST_ID = "post_id"
    const val COMMENTS = "comments"
    const val CONNECTIONS = "connections"
    const val RECEIVED_INVITES = "received_invites"
    const val SENT_INVITES = "sent_invites"

    // Search
    const val SEARCH_POSTFIX = "\uf8ff"
}