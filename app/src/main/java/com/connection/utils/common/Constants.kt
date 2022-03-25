package com.connection.utils.common

object Constants {

    // GetStream Api Key
    const val GET_STREAM_API_KEY = "th6wez2ccn4x"
    const val GET_STREAM_TOKEN =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoic3F1YXJlLWNlbGwtOCJ9.F6q7JMOahzFNs4iAMJ6ysfkfZijdxYPIZ7wrISdEbN8"

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

    // Channel Types
    const val CHANNEL_TYPE_MESSAGING = "messaging"

    // Navigation Models
    const val HEADER_MODEL = "header_model"

    // User Extra data
    const val USER_EXTRA_DATA_USERNAME = "username"
    const val USER_EXTRA_DATA_PICTURE = "picture"
    const val USER_EXTRA_DATA_DESCRIPTION = "description"

    // Channel Extra data
    const val EXTRA_DATA_CHANNEL_PICTURE = "channel_picture"
    const val EXTRA_DATA_CHANNEL_NAME = "channel_name"
    const val EXTRA_DATA_CHANNEL_STATUS = "channel_status"

    // Connection Statuses
    const val CONNECTION_STATUS_CONNECTED = "connected"
    const val CONNECTION_STATUS_NOT_CONNECTED = "not connected"
    const val CONNECTION_STATUS_PENDING = "pending"
}