package com.blogspot.android_czy_java.apps.mgr.main.db.model

import androidx.room.*

@Entity(
    tableName = "messages",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = CourseModel::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.NO_ACTION
        )
    )
)
class MessageModel(
    @PrimaryKey
    val firebaseId: String,
    val courseId: String,
    val message: String,
    val authorId: String,
    val timestamp: Long
)

class MessageWithAuthorModel(
    @Embedded
    val message: MessageModel,
    @Relation(parentColumn = "authorId", entityColumn = "id")
    val author: UserModel
)
