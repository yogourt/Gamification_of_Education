package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.google.api.client.auth.oauth2.RefreshTokenRequest
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAccessTokenFromRefreshToken @Inject constructor(private val prefs: SharedPreferences) {

    fun execute() = Single.create<Boolean> {

        val refreshToken = prefs.getString(PreferencesKeys.KEY_REFRESH_TOKEN, null)

        if(refreshToken == null) {
            it.onError(Exception())
        }
        try {
            val tokenResponse = RefreshTokenRequest(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                GenericUrl("https://oauth2.googleapis.com/token"),
                refreshToken
            ).execute()

            prefs.edit()
                .putLong(
                    PreferencesKeys.KEY_ACCESS_TOKEN_EXPIRATION_TIME_IN_MILLIS,
                    (System.currentTimeMillis()  + tokenResponse.expiresInSeconds * 1000)
                )
                .putString(PreferencesKeys.KEY_ACCESS_TOKEN, tokenResponse.accessToken)
                .putString(PreferencesKeys.KEY_REFRESH_TOKEN, tokenResponse.refreshToken)
                .apply()

            it.onSuccess(true)

        }catch (e: Exception) {
            it.onError(e)
        }
    }

}

