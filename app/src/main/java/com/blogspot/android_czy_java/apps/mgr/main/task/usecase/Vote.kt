package com.blogspot.android_czy_java.apps.mgr.main.task.usecase

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.MAX_POINTS_PER_DAY
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskCommentModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import javax.inject.Inject

class Vote @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun execute(comment: TaskCommentModel, upvote: Boolean): Boolean {

        if (isMaxPointsAdmittedToday()) {
            return false
        }

        FirebaseFunctions.getInstance()
            .getHttpsCallable(FirestoreKeys.CLOUD_FUNCTION_VOTE_FOR_COMMENT)
            .call(createUpvoteDataMap(comment, upvote)).addOnSuccessListener {
                incrementPointsAdmittedToday()
            }
        return true
    }

    private fun createUpvoteDataMap(comment: TaskCommentModel, upvote: Boolean): HashMap<String, Any> {
        return hashMapOf(
            FirestoreKeys.KEY_COMMENT_ID to comment.id,
            FirestoreKeys.KEY_AUTHOR_ID to comment.authorId,
            FirestoreKeys.KEY_UPVOTE to upvote
        )
    }

    private fun isMaxPointsAdmittedToday(): Boolean {
        return MAX_POINTS_PER_DAY - preferences.getInt(PreferencesKeys.KEY_POINTS_ADMITTED, 0) <= 0
    }

    private fun incrementPointsAdmittedToday() {
        val pointsAdmittedToday = preferences.getInt(PreferencesKeys.KEY_POINTS_ADMITTED, 0)
        preferences.edit().putInt(PreferencesKeys.KEY_POINTS_ADMITTED, pointsAdmittedToday + 1)
            .apply()
    }

}