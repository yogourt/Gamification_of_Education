package com.blogspot.android_czy_java.apps.mgr.main.task.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskCommentModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class ObserveFirestoreComments @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coursesDao: CoursesDao
) {

    private lateinit var taskId: String

    fun execute(taskId: String) {
        this.taskId = taskId
        firestore.collection(FirestoreKeys.COLLECTION_TASK_COMMENTS)
            .whereEqualTo(FirestoreKeys.TASK_ID, taskId)
            .addSnapshotListener { data, exception ->
                if (exception == null && data?.metadata?.hasPendingWrites() == false) {
                    saveComments(data)
                }
            }
    }

    private fun saveComments(snapshot: QuerySnapshot) {
        Thread {
            val commentsToAdd = mutableListOf<TaskCommentModel>()
            for (change in snapshot.documentChanges) {
                if (change.type == DocumentChange.Type.MODIFIED) {
                    updateTaskPoints(change.document)
                } else if (change.type == DocumentChange.Type.ADDED) {
                    commentsToAdd.add(convertToComment(change.document))
                }
            }
            val currentCommentsIds = coursesDao.getTaskCommentsIds(taskId)
            coursesDao.insertTaskComments(commentsToAdd.filter { !currentCommentsIds.contains(it.id) } )
        }.start()
    }

    private fun updateTaskPoints(snapshot: QueryDocumentSnapshot) {
        val commentHashMap = snapshot.data
        val commentId = snapshot.id
        val points = commentHashMap[FirestoreKeys.POINTS] as Long
        coursesDao.updateCommentPoints(points, commentId)
    }

    private fun convertToComment(snapshot: QueryDocumentSnapshot): TaskCommentModel {
        val commentHashMap = snapshot.data
        val commentId = snapshot.id
        return TaskCommentModel(
            commentId,
            commentHashMap[FirestoreKeys.USER_ID].toString(),
            commentHashMap[FirestoreKeys.TASK_ID].toString(),
            commentHashMap[FirestoreKeys.MESSAGE].toString(),
            commentHashMap[FirestoreKeys.LINK]?.toString(),
            commentHashMap[FirestoreKeys.TIMESTAMP] as Long,
            commentHashMap[FirestoreKeys.POINTS] as Long
        )
    }
}
