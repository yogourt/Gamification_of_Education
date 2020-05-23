package com.blogspot.android_czy_java.apps.mgr.main.login.usecase

import com.blogspot.android_czy_java.apps.mgr.main.db.model.UserModel
import com.blogspot.android_czy_java.apps.mgr.main.firebase.FirestoreKeys
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Emitter
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class SendUserToFirebaseIfNeeded @Inject constructor(private val firestore: FirebaseFirestore) {

    fun execute(user: UserModel): Single<UserModel> {

        return Single.create { emitter ->
            val userRef = firestore.collection(FirestoreKeys.COLLECTION_USERS).document(user.id)

            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result?.exists() != true) {
                    userRef.set(createUserDocument(user))
                    emitter.onSuccess(user)
                } else {
                    task.result?.let {
                        emitter.onSuccess(convertToUserModel(it, user.id))
                    }
                }
            }
        }
    }

    private fun convertToUserModel(snapshot: DocumentSnapshot, userId: String): UserModel {
        return UserModel(
            userId,
            snapshot[FirestoreKeys.KEY_USER_NICKNAME].toString(),
            snapshot[FirestoreKeys.KEY_USER_PHOTO]?.toString(),
            snapshot[FirestoreKeys.KEY_USER_POINTS] as? Long ?: 0
        )

    }

    private fun createUserDocument(user: UserModel) = hashMapOf(
        FirestoreKeys.KEY_USER_NICKNAME to user.nickname,
        FirestoreKeys.KEY_USER_PHOTO to user.photo,
        FirestoreKeys.KEY_USER_POINTS to user.points
    )


}