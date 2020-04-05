package com.blogspot.android_czy_java.apps.mgr.main.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blogspot.android_czy_java.apps.mgr.main.db.model.CourseModel

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: CourseModel)

    @Query("SELECT * FROM course")
    fun getCoursesLiveData(): LiveData<List<CourseModel>>

}