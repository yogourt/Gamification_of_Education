package com.blogspot.android_czy_java.apps.mgr.main.profile.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ChangeNickname @Inject constructor(private val userDao: UserDao, private val firestore: FirebaseFirestore) {

    fun execute(nickname: String) {
        Thread(Runnable {
            val currentUser = FirebaseAuth.getInstance().currentUser ?: return@Runnable

            userDao.updateNickname(currentUser.uid, nickname)

            firestore.collection(FirestoreKeys.COLLECTION_USERS).document(currentUser.uid).update(
                mapOf(FirestoreKeys.KEY_USER_NICKNAME to nickname)
            )
        }).start()
    }
}