package com.blogspot.android_czy_java.apps.mgr.main.profile

import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.profile.usecase.ChangeNickname
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    userDao: UserDao,
    private val changeNickname: ChangeNickname
) {

    val pointsLiveData = userDao.getCurrentUserPoints()
    val userLiveData = userDao.getCurrentUser(FirebaseAuth.getInstance().currentUser?.uid)

    fun changeNickname(nickname: String) = changeNickname.execute(nickname)

}
