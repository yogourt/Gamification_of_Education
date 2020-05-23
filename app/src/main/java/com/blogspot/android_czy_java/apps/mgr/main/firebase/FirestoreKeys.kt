package com.blogspot.android_czy_java.apps.mgr.main.firebase

class FirestoreKeys {

    companion object {
        const val COLLECTION_USERS = "Users"
        const val KEY_USER_NICKNAME = "nickname"
        const val KEY_USER_PHOTO = "photo"
        const val KEY_USER_POINTS = "points"
        const val USER_ID = "user_id"
        const val MESSAGE = "message"
        const val COURSE_ID = "course_id"
        const val TIMESTAMP = "timestamp"
        const val TASK_ID = "task_id"
        const val LINK = "link"
        const val POINTS = "points"

        const val COLLECTION_MESSAGES = "Messages"
        const val COLLECTION_TASK_COMMENTS = "Task_comments"

        //firebase functions keys
        const val KEY_UPVOTE = "upvote"
        const val KEY_COMMENT_ID = "commentId"
        const val KEY_MESSAGE_ID = "messageId"

        const val CLOUD_FUNCTION_VOTE_FOR_COMMENT = "voteForComment"
        const val CLOUD_FUNCTION_VOTE_FOR_MESSAGE = "voteForMessage"
    }

}
