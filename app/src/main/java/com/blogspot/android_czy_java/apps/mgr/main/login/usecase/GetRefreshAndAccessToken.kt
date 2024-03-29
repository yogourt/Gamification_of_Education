package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import android.content.SharedPreferences
import com.blogspot.android_czy_java.apps.mgr.main.PreferencesKeys
import com.blogspot.android_czy_java.apps.mgr.main.login.LoginPresenter
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetRefreshAndAccessToken @Inject constructor(private val prefs: SharedPreferences) {

    fun execute(): Single<Boolean> {
        return Single.create {

            val authCode = prefs.getString(PreferencesKeys.KEY_AUTH_CODE, null)
            if (authCode == null) {
                it.onError(Exception())
            }
            try {
                val authorizationFlow = GoogleAuthorizationCodeFlow.Builder(NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "413800448352-653qfbgp9h72jajpo7a08puhrv2ml28f.apps.googleusercontent.com",
                    "ZaC-tpfF1qRb_57kRGQF2sV6", LoginPresenter.scopes.map { it.toString() })
                    .build()

                val tokenRequest = authorizationFlow.newTokenRequest(authCode)
                tokenRequest["access_type"] = "offline"
                val tokenResponse = tokenRequest.execute()

                prefs.edit()
                    .putLong(
                        PreferencesKeys.KEY_ACCESS_TOKEN_EXPIRATION_TIME_IN_MILLIS,
                        (System.currentTimeMillis() + tokenResponse.expiresInSeconds * 1000)
                    )
                    .putString(PreferencesKeys.KEY_ACCESS_TOKEN, tokenResponse.accessToken)
                    .apply()

                tokenResponse.refreshToken?.let {
                    prefs.edit().putString(PreferencesKeys.KEY_REFRESH_TOKEN, it).apply()
                }

                it.onSuccess(true)
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

}