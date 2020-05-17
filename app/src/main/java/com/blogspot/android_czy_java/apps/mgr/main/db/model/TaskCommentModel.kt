package com.blogspot.android_czy_java.apps.mgr.main.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "taskComments")
class TaskCommentModel(
    @PrimaryKey
    val id: String,
    val authorId: String,
    val taskId: String,
    val message: String,
    val link: String?,
    val timestamp: Long,
    val points: Long
)

class TaskCommentWithAuthorModel(
    @Embedded
    val taskComment: TaskCommentModel,
    @Relation(parentColumn = "authorId", entityColumn = "id")
    val author: UserModel
)