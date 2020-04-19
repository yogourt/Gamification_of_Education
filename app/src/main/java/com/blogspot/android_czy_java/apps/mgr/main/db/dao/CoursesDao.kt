package com.blogspot.android_czy_java.apps.mgr.main.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.MessageWithAuthorModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel

@Dao
interface CoursesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: CourseModel)

    @Query("SELECT * FROM course")
    fun getCoursesLiveData(): LiveData<List<CourseModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(tasks: List<TaskModel>)

    @Query("SELECT * FROM tasks WHERE courseId=:courseId")
    fun getCourseTasksLiveData(courseId: String): LiveData<List<TaskModel>>

    @Query("SELECT * FROM messages WHERE courseId=:courseId ORDER BY timestamp")
    fun getCourseChatLiveData(courseId: String): LiveData<List<MessageWithAuthorModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMessages(messages: List<MessageModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMessage(message: MessageModel)

}