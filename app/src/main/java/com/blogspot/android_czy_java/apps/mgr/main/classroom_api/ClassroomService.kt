package com.blogspot.android_czy_java.apps.mgr.main.classroom_api

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.classroom.Classroom

class ClassroomService {

    companion object {

        fun getInstance(accessToken: String): Classroom {
            return Classroom(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                getCredentials(accessToken)
            )
        }

        private fun getCredentials(accessToken: String): GoogleCredential {
            val credential = GoogleCredential()
            credential.accessToken = accessToken
            return credential
        }
    }

}