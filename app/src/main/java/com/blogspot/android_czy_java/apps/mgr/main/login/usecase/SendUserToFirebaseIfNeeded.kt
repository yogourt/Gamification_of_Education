package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SendUserToFirebaseIfNeeded @Inject constructor(private val firestore: FirebaseFirestore) {

    fun execute(user: UserModel) {
        sendToFirebaseIfNeeded(user)
    }

    private fun sendToFirebaseIfNeeded(user: UserModel) {
        val userRef = firestore.collection(FirestoreKeys.COLLECTION_USERS).document(user.id)

        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result?.exists() != true) {
                userRef.set(createUserDocument(user))
            }
        }
    }

    private fun createUserDocument(user: UserModel) = hashMapOf(
        FirestoreKeys.KEY_USER_NICKNAME to user.nickname,
        FirestoreKeys.KEY_USER_PHOTO to user.photo
    )


}