package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SaveUserFromFirebase @Inject constructor(
    private val userDao: UserDao,
    private val sendUserToFirebaseIfNeeded: SendUserToFirebaseIfNeeded
) {

    fun execute(user: FirebaseUser, appUser: Boolean) {

        Thread {

            val mappedUser = UserModel(user.uid, user.displayName ?: "nickname", null)

            if (!userDao.containsUserId(user.uid)) {
                userDao.insertUser(mappedUser)
            }

            if (appUser) {
                sendUserToFirebaseIfNeeded.execute(mappedUser)
            }

        }.start()
    }

}
