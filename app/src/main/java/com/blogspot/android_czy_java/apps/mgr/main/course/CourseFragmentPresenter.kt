package com.blogspot.android_czy_java.apps.mgr.main.course

import androidx.lifecycle.LiveData
import com.blogspot.android_czy_java.apps.mgr.main.db.dao.CoursesDao
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskModel
import com.blogspot.android_czy_java.apps.mgr.main.db.model.TaskWithCommentsModel
import javax.inject.Inject

class CourseFragmentPresenter @Inject constructor(private val courseDao: CoursesDao) {

    lateinit var courseId:String

    fun getCourseTasks(): LiveData<List<TaskModel>> =
        courseDao.getCourseTasksLiveData(courseId)

    fun getCourseTitle(): LiveData<String> =
        courseDao.getCourseTitle(courseId)

    fun getActivityPoints(): LiveData<Int> =
        courseDao.getPoints(courseId)


}
