package com.blogspot.android_czy_java.apps.mgr.main.chat.usecase

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import javax.inject.Inject

class Vote @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun execute(messageId: String, upvote: Boolean = true) {
        FirebaseFunctions.getInstance()
            .getHttpsCallable(FirestoreKeys.CLOUD_FUNCTION_VOTE_FOR_MESSAGE)
            .call(createUpvoteDataMap(messageId, upvote)).addOnSuccessListener {
                incrementPointsAdmittedToday()
            }
    }

    private fun createUpvoteDataMap(messageId: String, upvote: Boolean): HashMap<String, Any> {
        return hashMapOf(
            FirestoreKeys.KEY_MESSAGE_ID to messageId,
            FirestoreKeys.KEY_UPVOTE to upvote
        )
    }

    private fun incrementPointsAdmittedToday() {
        val pointsAdmittedToday = preferences.getInt(PreferencesKeys.KEY_POINTS_ADMITTED, 0)
        preferences.edit().putInt(PreferencesKeys.KEY_POINTS_ADMITTED, pointsAdmittedToday + 1)
            .apply()
    }

}