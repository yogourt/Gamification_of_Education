package com.blogspot.android_czy_java.apps.mgr.main.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.blogspot.android_czy_java.apps.mgr.main.db.model.*

@Dao
interface CoursesDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertCourse(course: CourseModel)

    @Query("SELECT * FROM course")
    fun getCoursesLiveData(): LiveData<List<CourseModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(tasks: List<TaskModel>)

    @Query("SELECT * FROM tasks WHERE courseId=:courseId")
    fun getCourseTasksLiveData(courseId: String): LiveData<List<TaskModel>>

    @Query("SELECT * FROM tasks WHERE id=:taskId")
    fun getCourseTaskLiveData(taskId: String): LiveData<TaskWithCommentsModel>

    @Query("SELECT * FROM messages WHERE courseId=:courseId ORDER BY timestamp")
    fun getCourseChatLiveData(courseId: String): LiveData<List<MessageWithAuthorModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMessages(messages: List<MessageModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMessage(message: MessageModel)

    @Query("SELECT title FROM course WHERE id=:courseId")
    fun getCourseTitle(courseId: String): LiveData<String>

    @Query("UPDATE course SET points=:points WHERE id=:courseId")
    fun setPoints(courseId: String, points: Long)

    @Query("SELECT points FROM course WHERE id=:courseId")
    fun getPoints(courseId: String): LiveData<Long>

    @Query("SELECT points FROM course WHERE id=:courseId")
    fun getPointsValue(courseId: String): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertTaskComment(comment: TaskCommentModel)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertTaskComments(comments: List<TaskCommentModel>)

    @Query("SELECT id FROM taskComments WHERE taskId=:taskId")
    fun getTaskCommentsIds(taskId: String): List<String>

    @Query("UPDATE taskComments SET points=:points WHERE id=:commentId")
    fun updateCommentPoints(points: Long, commentId: String)

    @Query("SELECT firebaseId FROM messages WHERE courseId=:courseId")
    fun getChatMessagesIds(courseId: String): List<String>

    @Query("UPDATE messages SET points=:points WHERE firebaseId=:messageId")
    fun updateMessagePoints(points: Long, messageId: String)

}