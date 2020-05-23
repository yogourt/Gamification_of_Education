package com.blogspot.android_czy_java.apps.mgr.main.profile

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.UserDao
import com.blogspot.android_czy_java.apps.mgr.main.profile.usecase.ChangeNickname
import com.blogspot.android_czy_java.apps.mgr.main.profile.usecase.ObserveFirestorePoints
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    userDao: UserDao,
    private val changeNickname: ChangeNickname,
    private val preferences: SharedPreferences,
    private val observeFirestorePoints: ObserveFirestorePoints
) {


    fun init() {
        observeFirestorePoints.execute()
    }

    val pointsLiveData = userDao.getCurrentUserPoints()
    val userLiveData = userDao.getCurrentUser(FirebaseAuth.getInstance().currentUser?.uid)

    fun changeNickname(nickname: String) = changeNickname.execute(nickname)

    fun todayAdmittedPoints(): Int {
        return preferences.getInt(PreferencesKeys.KEY_POINTS_ADMITTED, 0)
    }

}
