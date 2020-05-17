package com.blogspot.android_czy_java.apps.mgr.main.task.usecase

import com.blogspot.android_czy_java.apps.mgr.main.chat.exception.MessageNotSentException
import com.blogspot.android_czy_java.apps.mgr.main.chat.exception.UserIsNullException
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskCommentModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SendComment @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coursesDao: CoursesDao
) {

    fun execute(message: String, link: String?, taskId: String): Single<Boolean> =
        Single.create { emitter ->

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId == null) {
                emitter.onError(UserIsNullException())
            } else {

                val timestamp = System.currentTimeMillis()
                val linkProcessed = if(link != null && !link.startsWith("http")) "http://${link}" else link

                firestore.collection(FirestoreKeys.COLLECTION_TASK_COMMENTS)
                    .add(createCommentHashMap(message, linkProcessed, taskId, userId, timestamp))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            insertCommentToDb(task.result?.id, message, linkProcessed, userId, taskId, timestamp)
                            emitter.onSuccess(true)
                        } else emitter.onError(MessageNotSentException())
                    }
            }
        }

    private fun insertCommentToDb(
        id: String?,
        message: String,
        link: String?,
        userId: String,
        taskId: String,
        timestamp: Long
    ) {
        id?.let {
            Thread {
                coursesDao.insertTaskComment(
                    TaskCommentModel(it, userId, taskId, message, link, timestamp, 0)
                )
            }.start()
        }
    }

    private fun createCommentHashMap(
        message: String,
        link: String?,
        taskId: String,
        userId: String,
        timestamp: Long
    ): HashMap<String, Any> {
        val map = hashMapOf<String, Any>(
            FirestoreKeys.USER_ID to userId,
            FirestoreKeys.TASK_ID to taskId,
            FirestoreKeys.MESSAGE to message,
            FirestoreKeys.TIMESTAMP to timestamp,
            FirestoreKeys.POINTS to 0
        )
        link?.let { map[FirestoreKeys.LINK] = link }
        return map
    }

}