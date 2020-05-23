package com.blogspot.android_czy_java.apps.mgr.main.profile.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ObserveFirestorePoints @Inject constructor(private val firestore: FirebaseFirestore,
private val userDao: UserDao) {

    fun execute() {

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        firestore.collection(FirestoreKeys.COLLECTION_USERS)
            .document(userId)
            .addSnapshotListener { data, exception ->
                if (exception == null) {
                    savePoints(data, userId)
                }
            }
    }

    private fun savePoints(
        data: DocumentSnapshot?,
        userId: String
    ) {
        Thread {
            val points = data?.get(FirestoreKeys.POINTS) as? Long
            points?.let {
                userDao.updatePoints(userId, points)
            }
        }.start()
    }
}
