package com.blogspot.android_czy_java.apps.mgr.main.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = CourseModel::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    )
)
class TaskModel(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val title: String?,
    val description: String?,
    val completed: Boolean
)
