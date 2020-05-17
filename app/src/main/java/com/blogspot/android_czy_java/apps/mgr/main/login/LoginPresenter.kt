package com.blogspot.android_czy_java.apps.mgr.main.login

import android.content.SharedPreferences
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blogspot.android_czy_java.apps.mgr.R
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.blogspot.android_czy_java.apps.mgr.main.classroom_api.FetchCoursesDataUseCase
import com.blogspot.android_czy_java.apps.mgr.main.login.usecase.GetAccessToken
import com.blogspot.android_czy_java.apps.mgr.main.login.usecase.SaveUserFromFirebase
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.classroom.ClassroomScopes
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val getAccessToken: GetAccessToken,
    private val fetchCoursesDataUseCase: FetchCoursesDataUseCase,
    private val preferences: SharedPreferences,
    private val saveUserFromFirebase: SaveUserFromFirebase
) {


    companion object {
        val scopes = arrayOf(
            Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY),
            Scope(ClassroomScopes.CLASSROOM_COURSEWORK_ME)
        )
    }

    fun isFetchNeeded(): Boolean {
        return (System.currentTimeMillis() - preferences.getLong(
            PreferencesKeys.KEY_LAST_CLASSROOM_API_FETCH,
            0
        )) > TimeUnit.HOURS.toMillis(
            6
        )
    }

    fun isNewUser(): Boolean {
        return !preferences.contains(PreferencesKeys.KEY_ACCESS_TOKEN)
    }

    fun fetchFromClassroomApi() {
        getAccessToken.execute()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    fetchState.value = true
                    fetchCoursesDataUseCase.execute()
                },
                { fetchState.value = false })
    }

    fun saveAuthCodeAndProcess(authCode: String) {
        preferences.edit().putString(PreferencesKeys.KEY_AUTH_CODE, authCode).apply()
        fetchFromClassroomApi()
    }

    fun getCredentialForFirebase(idToken: String) = GoogleAuthProvider.getCredential(idToken, null)

    fun saveUser(user: FirebaseUser) {
        saveUserFromFirebase.execute(user, true)
    }

    fun resetPointsIfNeeded() {
        if (isPointResetNeeded()) {
            resetPoints()
        }
    }

    private fun isPointResetNeeded(): Boolean {
        return System.currentTimeMillis() - preferences.getLong(
            PreferencesKeys.KEY_LAST_POINTS_RESET,
            0
        ) > TimeUnit.DAYS.toMillis(1)
    }

    private fun resetPoints() {
        preferences.edit().putInt(PreferencesKeys.KEY_POINTS_ADMITTED, 0)
            .putLong(PreferencesKeys.KEY_LAST_POINTS_RESET, System.currentTimeMillis()).apply()
    }

    val fetchState = MutableLiveData<Boolean>()

    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestScopes(scopes[0], scopes[1])
        .requestServerAuthCode("413800448352-653qfbgp9h72jajpo7a08puhrv2ml28f.apps.googleusercontent.com")
        .requestIdToken("413800448352-653qfbgp9h72jajpo7a08puhrv2ml28f.apps.googleusercontent.com")
        .build()
}


