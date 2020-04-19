package com.blogspot.android_czy_java.apps.mgr.main.chat.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.api.ChangeType
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class ObserveFirestoreMessages @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coursesDao: CoursesDao
) {

    fun execute(courseId: String) {
        firestore.collection(FirestoreKeys.COLLECTION_MESSAGES)
            .whereEqualTo(FirestoreKeys.COURSE_ID, courseId)
            .addSnapshotListener { data, exception ->
                if (exception == null && data?.metadata?.hasPendingWrites() == false) {
                    saveMessages(data)
                }
            }
    }

    private fun saveMessages(snapshot: QuerySnapshot) {
        Thread {
            val messagesToAdd = mutableListOf<MessageModel>()
            for (change in snapshot.documentChanges) {
                if (change.type == DocumentChange.Type.ADDED) {
                    messagesToAdd.add(convertToMessage(change.document))
                }
            }
            coursesDao.insertChatMessages(messagesToAdd)
        }.start()
    }

    private fun convertToMessage(snapshot: QueryDocumentSnapshot): MessageModel {
        val messageHashMap = snapshot.data
        val messageId = snapshot.id
        return MessageModel(
            messageId,
            messageHashMap[FirestoreKeys.COURSE_ID].toString(),
            messageHashMap[FirestoreKeys.MESSAGE].toString(),
            messageHashMap[FirestoreKeys.USER_ID].toString(),
            messageHashMap[FirestoreKeys.TIMESTAMP] as Long
        )
    }

}