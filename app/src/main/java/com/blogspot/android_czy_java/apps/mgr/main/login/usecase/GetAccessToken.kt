package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import javax.inject.Inject

class GetAccessToken @Inject constructor(
    private val getAccessTokenFromRefreshToken: GetAccessTokenFromRefreshToken,
    private val getRefreshAndAccessToken: GetRefreshAndAccessToken,
    private val preferences: SharedPreferences
) {

    fun execute() = Single.create<String> { emitter ->

        val accessTokenExists = preferences.contains(PreferencesKeys.KEY_ACCESS_TOKEN)

        val isExpired =
            (preferences.getLong(
                PreferencesKeys.KEY_ACCESS_TOKEN_EXPIRATION_TIME_IN_MILLIS,
                0
            ) - System.currentTimeMillis()) < 0

        if (accessTokenExists) {
            if (isExpired) getAccessTokenFromRefreshToken.execute().subscribe(
                { emitAccessToken(emitter) },
                { getRefreshToken(emitter) }
            )
            else emitAccessToken(emitter)
        } else {
            getRefreshToken(emitter)
        }
    }

    private fun emitAccessToken(emitter: SingleEmitter<String>) {
        val accessToken = preferences.getString(PreferencesKeys.KEY_ACCESS_TOKEN, null)
        accessToken?.let { emitter.onSuccess(it) } ?: emitter.onError(Exception())
    }


    private fun getRefreshToken(emitter: SingleEmitter<String>) {
        getRefreshAndAccessToken.execute().subscribe(
            { emitAccessToken(emitter) },
            { emitter.onError(it) }
        )
    }

}