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

    @Query("UPDATE course SET activityPoints=:points WHERE id=:courseId")
    fun setPoints(courseId: String, points: Int)

    @Query("SELECT activityPoints FROM course WHERE id=:courseId")
    fun getPoints(courseId: String): LiveData<Int>

    @Query("SELECT activityPoints FROM course WHERE id=:courseId")
    fun getPointsValue(courseId: String): Int

}