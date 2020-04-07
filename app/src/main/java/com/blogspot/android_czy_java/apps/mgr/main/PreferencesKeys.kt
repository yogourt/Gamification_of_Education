package com.blogspot.android_czy_java.apps.mgr.main

interface PreferencesKeys {

    companion object {
        const val KEY_AUTH_CODE = "auth code"
        const val KEY_ACCESS_TOKEN_EXPIRATION_TIME_IN_MILLIS = "access token expiration"
        const val KEY_ACCESS_TOKEN = "access token"
        const val KEY_REFRESH_TOKEN = "refresh token"
        const val KEY_LAST_CLASSROOM_API_FETCH = "last classroom api fetch"
    }
}