package com.blogspot.android_czy_java.apps.mgr.main.chat.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.firestore.*
import javax.inject.Inject

class ObserveFirestoreMessages @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val coursesDao: CoursesDao,
    private val userDao: UserDao
) {

    private lateinit var courseId: String

    fun execute(courseId: String) {
        this.courseId = courseId
        firestore.collection(FirestoreKeys.COLLECTION_MESSAGES)
            .whereEqualTo(FirestoreKeys.COURSE_ID, courseId)
            .addSnapshotListener { data, exception ->
                if (exception == null && data?.metadata?.hasPendingWrites() == false) {
                    processData(data)
                }
            }
    }

    private fun processData(snapshot: QuerySnapshot) {
        Thread {
            val messagesToAdd = mutableListOf<MessageModel>()
            val newUserIds = mutableListOf<String>()
            for (change in snapshot.documentChanges) {
                if (change.type == DocumentChange.Type.MODIFIED) {
                    updateMessagePoints(change.document)
                }
                if (change.type == DocumentChange.Type.ADDED) {
                    val message = convertToMessage(change.document)
                    messagesToAdd.add(message)
                    if (!userDao.containsUserId(message.authorId)) {
                        newUserIds.add(message.authorId)
                    }
                }
            }
            saveMessagesAndUsers(messagesToAdd, newUserIds)
        }.start()
    }

    private fun saveMessagesAndUsers(
        messagesToAdd: MutableList<MessageModel>,
        newUserIds: MutableList<String>
    ) {
        val usersToAdd = mutableListOf<UserModel>()
        firestore.collection(FirestoreKeys.COLLECTION_USERS).get()
            .addOnSuccessListener { snapshot ->
                for (userId in newUserIds) {
                    usersToAdd.add(convertToUser(snapshot.documents.first { it.id == userId }))
                }

                Thread {
                    userDao.insertUsers(usersToAdd)
                    saveMessages(messagesToAdd)
                }.start()
            }
    }

    private fun saveMessages(
        messagesToAdd: MutableList<MessageModel>
    ) {
        val currentMessagesIds = coursesDao.getChatMessagesIds(courseId)
        messagesToAdd.removeAll { currentMessagesIds.contains(it.firebaseId) }
        coursesDao.insertChatMessages(messagesToAdd)
    }

    private fun updateMessagePoints(snapshot: QueryDocumentSnapshot) {
        val messageHashMap = snapshot.data
        val messageId = snapshot.id
        val points = messageHashMap[FirestoreKeys.POINTS] as Long
        coursesDao.updateMessagePoints(points, messageId)
    }


    private fun convertToMessage(snapshot: QueryDocumentSnapshot): MessageModel {
        val messageHashMap = snapshot.data
        val messageId = snapshot.id
        return MessageModel(
            messageId,
            messageHashMap[FirestoreKeys.COURSE_ID].toString(),
            messageHashMap[FirestoreKeys.MESSAGE].toString(),
            messageHashMap[FirestoreKeys.USER_ID].toString(),
            messageHashMap[FirestoreKeys.POINTS] as? Long ?: 0,
            messageHashMap[FirestoreKeys.TIMESTAMP] as Long
        )
    }

    private fun convertToUser(snapshot: DocumentSnapshot): UserModel {
        return UserModel(
            snapshot.id,
            snapshot[FirestoreKeys.KEY_USER_NICKNAME].toString(),
            snapshot[FirestoreKeys.KEY_USER_PHOTO]?.toString(),
            snapshot[FirestoreKeys.KEY_USER_POINTS] as? Long ?: 0
        )
    }

}
