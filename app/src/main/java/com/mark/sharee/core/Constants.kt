package com.mark.sharee.core

class Constants {
    companion object {
        const val BASE_URL = "http://192.168.1.162:2304/"
//        const val BASE_URL = "http://192.168.14.121:2304/"
        const val CACHE_FILE_SIZE : Long = 25 * 1024 * 1024 //25MB
        const val CACHE_IMAGE_SIZE : Long = 25 * 1024 * 1024 //25MB

        const val TIMEOUT_LENGTH = 60

        const val SHAREE_PUSH_CHANNEL = "sharee"
        const val NOTIFICATION_ID = 1717

        const val NOTIFICATION_INTENT_ACTION = "sharee.SCHEDULED_NOTIFICATION"
    }
}