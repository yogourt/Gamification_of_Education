package com.blogspot.android_czy_java.apps.mgr.main.profile.avatar.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChangeAvatar @Inject constructor(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) {

    fun execute(id: String) {
        Thread(Runnable {
            val currentUser = FirebaseAuth.getInstance().currentUser ?: return@Runnable
            userDao.updateAvatar(id, currentUser.uid)

            firestore.collection(FirestoreKeys.COLLECTION_USERS).document(currentUser.uid).set(
                hashMapOf(FirestoreKeys.KEY_USER_PHOTO to id)
            )
        }).start()
    }
}