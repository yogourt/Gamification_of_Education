package com.blogspot.android_czy_java.apps.mgr.main.db.model

import androidx.room.*

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

class TaskWithCommentsModel(
    @Embedded
    val task: TaskModel,
    @Relation(parentColumn = "id", entityColumn = "taskId", entity = TaskCommentModel::class)
    val comments: List<TaskCommentWithAuthorModel>
)
