package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SaveUserFromFirebase @Inject constructor(
    private val userDao: UserDao,
    private val sendUserToFirebaseIfNeeded: SendUserToFirebaseIfNeeded
) {

    fun execute(user: FirebaseUser, appUser: Boolean) {

        Thread {

            val mappedUser = UserModel(user.uid, user.displayName ?: "nickname", null, 0)


            if (appUser) {
                sendUserToFirebaseIfNeeded.execute(mappedUser).subscribe { user ->
                    Thread {
                        if (!userDao.containsUserId(user.id)) {
                            userDao.insertUser(user)
                        }
                    }.start()
                }
            }

        }.start()
    }

}
