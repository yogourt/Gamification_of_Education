package com.blogspot.android_czy_java.apps.mgr.main.classroom_api

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.classroom.Classroom
import io.reactivex.rxjava3.core.Single

class ClassroomService {

    companion object {

        @Volatile
        private lateinit var instance: Classroom

        fun getInstance(): Classroom {
            return instance
        }

        fun init(authCode: String, clientId: String, clientSecret: String): Single<Boolean> =
            Single.create {
                try {
                    val credentials = getCredentials(authCode, clientId, clientSecret)
                    instance = Classroom(
                        NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        credentials
                    )
                    it.onSuccess(true)
                } catch (e: Exception) {
                    it.onError(e)
                }
            }


        private fun getCredentials(authCode: String, clientId: String, clientSecret: String): GoogleCredential {

            val tokenResponse = GoogleAuthorizationCodeTokenRequest(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://oauth2.googleapis.com/token",
                clientId,
                clientSecret,
                authCode,
                ""
            )
                .execute()

            val accessToken = tokenResponse.accessToken
            val credential = GoogleCredential()
            credential.accessToken = accessToken
            return credential
        }
    }

}