package com.blogspot.android_czy_java.apps.mgr.main.chat.usecase

import com.blogspot.android_czy_java.apps.mgr.main.chat.exception.MessageNotSentException
import com.blogspot.android_czy_java.apps.mgr.main.chat.exception.UserIsNullException
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SendChatMessage @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coursesDao: CoursesDao
) {

    fun execute(message: MessageWithCourseId): Single<Boolean> = Single.create { emitter ->

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            emitter.onError(UserIsNullException())
        } else {

            val timestamp = System.currentTimeMillis()

            firestore.collection(FirestoreKeys.COLLECTION_MESSAGES)
                .add(createMessageHashMap(message, userId, timestamp)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        insertMessageToDb(task.result?.id, message, userId, timestamp)
                        emitter.onSuccess(true)
                    } else emitter.onError(MessageNotSentException())
                }
        }
    }

    private fun insertMessageToDb(
        id: String?,
        message: MessageWithCourseId,
        userId: String,
        timestamp: Long
    ) {
        id?.let {
            Thread {
                coursesDao.insertChatMessage(
                    MessageModel(it, message.courseId, message.message, userId, timestamp)
                )
            }.start()
        }
    }

    private fun createMessageHashMap(
        message: MessageWithCourseId,
        userId: String,
        timestamp: Long
    ): HashMap<String, Any> {
        return hashMapOf(
            FirestoreKeys.USER_ID to userId,
            FirestoreKeys.COURSE_ID to message.courseId,
            FirestoreKeys.MESSAGE to message.message,
            FirestoreKeys.TIMESTAMP to timestamp
        )
    }

}

class MessageWithCourseId(val message: String, val courseId: String)
